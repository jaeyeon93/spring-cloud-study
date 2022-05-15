package me.jimmy.catalogs.service;

import me.jimmy.catalogs.entity.CatalogEntity;
import me.jimmy.catalogs.repository.CatalogRepository;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl implements CatalogService {
    private CatalogRepository repository;

    public CatalogServiceImpl(CatalogRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return repository.findAll();
    }
}
