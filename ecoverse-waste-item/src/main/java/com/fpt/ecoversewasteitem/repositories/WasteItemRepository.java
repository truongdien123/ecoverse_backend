package com.fpt.ecoversewasteitem.repositories;

import com.fpt.ecoversewasteitem.entities.WasteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WasteItemRepository extends JpaRepository<WasteItem, String> {

    List<WasteItem> findByCorrectBinCode(String correctBinCode);

    List<WasteItem> findByNameContainingIgnoreCase(String name);
}
