package me.jimmy.catalogs.service;

import me.jimmy.catalogs.entity.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
