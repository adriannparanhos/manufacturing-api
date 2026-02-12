package br.com.production.service;

import br.com.production.domain.model.RawMaterial;
import br.com.production.domain.repository.RawMaterialRepository;
import br.com.production.rest.dto.RawMaterialDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RawMaterialService {

    private final RawMaterialRepository repository;

    public RawMaterialService(RawMaterialRepository repository) {
        this.repository = repository;
    }

    public List<RawMaterialDTO> listAll() {
        return repository.listAll().stream()
                .map(entity -> new RawMaterialDTO(
                        entity.getId(),   // Usar Getter
                        entity.getName(), // Usar Getter
                        entity.getStockQuantity() // Usar Getter
                ))
                .collect(Collectors.toList());
    }

    public RawMaterialDTO findById(Long id) {
        RawMaterial entity = repository.findById(id);
        if (entity == null) return null;
        return new RawMaterialDTO(entity.getId(), entity.getName(), entity.getStockQuantity());
    }

    @Transactional
    public RawMaterialDTO create(RawMaterialDTO dto) {
        RawMaterial entity = new RawMaterial(dto.name(), dto.stockQuantity());

        repository.persist(entity);
        return new RawMaterialDTO(entity.getId(), entity.getName(), entity.getStockQuantity());
    }

    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        RawMaterial entity = repository.findById(id);
        if (entity == null) return null;

        entity.updateDetails(dto.name(), dto.stockQuantity());

        return new RawMaterialDTO(entity.getId(), entity.getName(), entity.getStockQuantity());
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}