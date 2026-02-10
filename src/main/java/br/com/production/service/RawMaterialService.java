package br.com.production.service;

import br.com.production.domain.model.RawMaterial;
import br.com.production.domain.repository.RawMaterialRepository;
import br.com.production.rest.dto.RawMaterialDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RawMaterialService {

    @Inject
    RawMaterialRepository repository;

    public List<RawMaterialDTO> listAll() {
        return repository.listAll().stream()
                .map(entity -> new RawMaterialDTO(entity.id, entity.name, entity.stockQuantity))
                .collect(Collectors.toList());
    }

    public RawMaterialDTO findById(Long id) {
        RawMaterial entity = repository.findById(id);
        if (entity == null) return null;
        return new RawMaterialDTO(entity.id, entity.name, entity.stockQuantity);
    }

    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        RawMaterial entity = new RawMaterial();
        entity.name = dto.name();
        entity.stockQuantity = dto.stockQuantity();
        entity.code = "RM-" + System.currentTimeMillis();

        repository.persist(entity);
        return new RawMaterialDTO(entity.id, entity.name, entity.stockQuantity);
    }

    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        RawMaterial entity = repository.findById(id);
        if (entity == null) return null;

        entity.name = dto.name();
        entity.stockQuantity = dto.stockQuantity();

        return new RawMaterialDTO(entity.id, entity.name, entity.stockQuantity);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
