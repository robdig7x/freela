package com.prox.reservas.firebase.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.NoRepositoryBean;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@NoRepositoryBean
public interface FireStoreRepository<T> {
	
	static final String ID = "id";
	final Firestore dbFireStore = FirestoreClient.getFirestore();
	final Logger log = LoggerFactory.getLogger(FireStoreRepository.class);
	
	default List<T> findAll() {
		ApiFuture<QuerySnapshot> future = dbFireStore.collection(getNameCollectionForActualClass()).get();
		
		List<T> salas = null;
		
		try {
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();
			
			salas = documents
				.stream()
				.filter(d -> !d.getId().contains("--stats--"))
				.map(d -> d.toObject(getParametizadeClass()))
				.collect(Collectors.toList());
			
		} catch (InterruptedException | ExecutionException e) {
			log.warn("Interrupted!", e);
		    Thread.currentThread().interrupt();
		}
		return salas;
	}
	
	default void saveWithUniqueId(T data) throws InterruptedException, ExecutionException {
		
		CollectionReference collection = dbFireStore.collection(getNameCollectionForActualClass());
		DocumentReference documentWithIdOrElse;
		
		Object newId = null;
		Map<String, Object> obtainIdFromData = obtainIdFromData(data);
		Object idValue = obtainIdFromData.get(ID);
		boolean possuiID = obtainIdFromData.containsKey(ID);

		//Objeto possui Campo ID?
		if (possuiID) {
			if (Objects.isNull(idValue)) {
				final DocumentReference document = collection.document("--stats--");
				document.set(Map.of("count", FieldValue.increment(1)), SetOptions.merge()).get();
				
				// asynchronously retrieve the document
				final ApiFuture<DocumentSnapshot> future = document.get();
				// ...
				// future.get() blocks on response
				final DocumentSnapshot statsCollecttion = future.get();
				if (statsCollecttion.exists()) {
					log.info("Recuperando ID = {}", statsCollecttion.getData());
					try {
						newId = (getParametizadeClass().getDeclaredField(ID).getType()).cast(statsCollecttion.getData().get("count"));
						Field field = getParametizadeClass().getDeclaredField(ID);
						field.setAccessible(true);
						field.set(data, newId);
					} catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
						log.error("Erro ao realizar parse do objeto {} ", e);
					}
				}
			} else {
				newId = idValue;
			}
			documentWithIdOrElse = collection.document(String.valueOf(newId));
		} else {
			documentWithIdOrElse = collection.document();
		}
		 
		
		ApiFuture<WriteResult> apiFuture = documentWithIdOrElse.set(data);
		
		log.info("Objeto {} => create : {} - {} ", getNameCollectionForActualClass(), data, apiFuture.get().getUpdateTime());
		
	}
	
	default Map<String, Object> obtainIdFromData(T data) {
		Map<String, Object> id = new HashMap<>(); 
		try {
			Field field = getParametizadeClass().getDeclaredField(ID);
			field.setAccessible(true);
			Long idValue = (Long) field.get(data);
			id.put(ID, idValue);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			log.error("ID não encontrado para o Obj {}", data);
		}
		
		return id;
	}

	default Optional<T> findById(Long id) {
		
		String collection = getNameCollectionForActualClass();
		
		DocumentReference docRef = dbFireStore.collection(collection).document(id.toString());
		
		// asynchronously retrieve the document
		ApiFuture<DocumentSnapshot> future = docRef.get();
		// ...
		// future.get() blocks on response
		DocumentSnapshot document = null;
		try {
			document = future.get();
			if (document.exists()) {
				log.info("{} encontrada = {}", collection, document.getData());
				return Optional.of(document.toObject(getParametizadeClass()));
			} 
		} catch (InterruptedException | ExecutionException e) {
			log.warn("Interrupted!", e);
		    Thread.currentThread().interrupt();
		}
		
		return Optional.empty();
	}

	default void deleteById(Long id) {
		try {
			dbFireStore.collection(getNameCollectionForActualClass()).document(id.toString()).delete().get();
		} catch (InterruptedException | ExecutionException e) {
			log.warn("Interrupted!", e);
		    Thread.currentThread().interrupt();
		}
	}
	
	default void save(T data) {
		try {
			saveWithUniqueId(data);
		} catch (InterruptedException | ExecutionException e) {
			log.warn("Interrupted!", e);
		    Thread.currentThread().interrupt();
		}
	}
	
	default void delete(T data) {
		Map<String, Object> dataId = obtainIdFromData(data);
		Long id = (Long) dataId.get(ID);
		deleteById(id);
	}

	@SuppressWarnings("unchecked")
	default Class<T> getParametizadeClass() {
		Type genericType = null;
		Type[] genericInterfaces = getClass().getGenericInterfaces();
		for (Type genericInterface : genericInterfaces) {
		    if (genericInterface instanceof ParameterizedType) {
		        Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
		        genericType = genericTypes[0];
		    }
		}
		return (Class<T>) genericType;
	}
	
	default String getNameCollectionForActualClass() {
		String collection = getParametizadeClass().getSimpleName().toLowerCase();
		while (true) {
			if (collection.endsWith("dto")) {
				collection = collection.replace("dto", "");
			} else {
				break;
			}
		}
		return collection;
	}

}
