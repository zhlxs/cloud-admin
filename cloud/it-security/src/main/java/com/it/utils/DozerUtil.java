package com.it.utils;


import org.dozer.Mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DozerUtil {
    private Mapper mapper;

    public DozerUtil(Mapper mapper) {
        this.mapper = mapper;
    }

    public Mapper getMapper() {
        return this.mapper;
    }

    public <T> T map(Object source, Class<T> destinationClass) {
        return source == null ? null : this.mapper.map(source, destinationClass);
    }

    public <T> T map2(Object source, Class<T> destinationClass) {
        if (source == null) {
            try {
                return destinationClass.newInstance();
            } catch (Exception e) {

            }
        }
        return this.mapper.map(source, destinationClass);
    }

    public void map(Object source, Object destination) {
        if (source != null) {
            this.mapper.map(source, destination);
        }
    }

    public <T> T map(Object source, Class<T> destinationClass, String mapId) {
        return source == null ? null : this.mapper.map(source, destinationClass, mapId);
    }

    public void map(Object source, Object destination, String mapId) {
        if (source != null) {
            this.mapper.map(source, destination, mapId);
        }
    }

    public <T, E> List<T> mapPage(Collection<E> sourceList, Class<T> destinationClass) {
        if (sourceList != null && !sourceList.isEmpty() && destinationClass != null) {
            List<T> destinationList = (List) sourceList.parallelStream().filter(item -> {
                return item != null;
            }).map(source -> {
                return this.mapper.map(source, destinationClass);
            }).collect(Collectors.toList());
            return destinationList;
        } else {
            return Collections.emptyList();
        }
    }

    public <T, E> Set<T> mapSet(Collection<E> sourceList, Class<T> destinationClass) {
        return sourceList != null && !sourceList.isEmpty() && destinationClass != null ? (Set) sourceList.parallelStream().map(source -> {
            return this.mapper.map(source, destinationClass);
        }).collect(Collectors.toSet()) : Collections.emptySet();
    }
}
