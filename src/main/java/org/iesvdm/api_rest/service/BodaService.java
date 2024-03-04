package org.iesvdm.api_rest.service;

import org.iesvdm.api_rest.domain.Boda;
import org.iesvdm.api_rest.exception.EntityNotFoundException;
import org.iesvdm.api_rest.exception.NotCouplingIdException;
import org.iesvdm.api_rest.repository.BodaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BodaService {

    @Autowired
    private BodaRepository bodaRepository;

    public List<Boda> all(){return this.bodaRepository.findAll();}

    public Boda save(Boda boda){return this.bodaRepository.save(boda);}

    public Boda one(Long id){
        return this.bodaRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(id, Boda.class));
    }

    public Boda replace(Long id, Boda boda){
        return this.bodaRepository.findById(id).map( b -> {
            if (id.equals(boda.getId())) return this.bodaRepository.save(boda);
            else throw new NotCouplingIdException(id, boda.getId(), Boda.class);
        }).orElseThrow(()-> new EntityNotFoundException(id, Boda.class));
    }

    public void delete(Long id){
        this.bodaRepository.findById(id).map( b -> {this.bodaRepository.delete(b);
        return b;})
                .orElseThrow(()-> new EntityNotFoundException(id, Boda.class));
    }
}