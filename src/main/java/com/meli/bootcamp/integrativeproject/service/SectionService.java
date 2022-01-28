package com.meli.bootcamp.integrativeproject.service;

import com.meli.bootcamp.integrativeproject.entity.Section;
import com.meli.bootcamp.integrativeproject.repositories.SectionRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {

    private SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    public Section findById(Long id) {
        return sectionRepository.findById(id).orElseThrow(() -> new RuntimeException("NOT FOUND"));
    }

    public Section save(Section section) {
        return sectionRepository.save(section);
    }

    public void deleteById(Long id) {
        sectionRepository.deleteById(id);
    }

}
