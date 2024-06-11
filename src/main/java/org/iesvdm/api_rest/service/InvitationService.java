package org.iesvdm.api_rest.service;

import org.iesvdm.api_rest.domain.Invitation;
import org.iesvdm.api_rest.dto.InvitationDTO;
import org.iesvdm.api_rest.exception.EntityNotFoundException;
import org.iesvdm.api_rest.exception.NotCouplingIdException;
import org.iesvdm.api_rest.mapper.InvitationMapper;
import org.iesvdm.api_rest.repository.InvitationRepository;
import org.iesvdm.api_rest.util.PaginationTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvitationService {

    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    InvitationMapper invitationMapper;

    public List<Invitation> all(){return this.invitationRepository.findAll();}

    // Pagination of All data:
    public Map<String, Object> all(int page, int size){
        Pageable paginator = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Invitation> pageAll = this.invitationRepository.findAll(paginator);

        return PaginationTool.createPaginatedResponseMap(pageAll, "invitations");
    }

    // Find by filter and return paginated:
    public Map<String, Object> findByFilter(int page, int size, String filter){
        Pageable paginator = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Invitation> pageFiltered = this.invitationRepository
                .findInvitationByNameContainingIgnoreCase(filter, paginator);

        return PaginationTool.createPaginatedResponseMap(pageFiltered, "invitations");
    }

    // Fetch the user's email (Wedding owner) based on the invitation ID:
    public String getUserEmailByInvitationId(Long invitationId) {
        Optional<String> userEmail = invitationRepository.findUserEmailByInvitationId(invitationId);
        String output = "";
        if(userEmail.isPresent()){
            output = userEmail.get();
        }
        return output;
    }

    public Invitation save(Invitation invitation){
        return this.invitationRepository.save(invitation);
    }

    public Invitation one(Long id){
        return this.invitationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(id, Invitation.class));
    }

    public InvitationDTO oneMapped(Long id){

        return this.invitationRepository.findById(id).map(invitationMapper::mapToDto)
                .orElseThrow(()-> new EntityNotFoundException(id, Invitation.class));

//        System.out.println("Looking for invitation");
//        Invitation invitation = this.invitationRepository.findById(id)
//                .orElseThrow(()-> new EntityNotFoundException(id, Invitation.class));
//        System.out.println("Invitation found: " + invitation);
//
//        System.out.println("Mapping invitation to dto:");
//        InvitationDTO dto = this.invitationMapper.mapToDto(invitation);
//
//        System.out.println("Dto created: " + dto);
//        return dto;
    }

    public Invitation replace(Long id, Invitation invitation){
        return this.invitationRepository.findById(id).map(m -> {
            if (id.equals(invitation.getId())) return this.invitationRepository.save(invitation);
            else throw new NotCouplingIdException(id, invitation.getId(), Invitation.class);
        }).orElseThrow(()-> new EntityNotFoundException(id, Invitation.class));
    }

    public void delete(Long id){
        this.invitationRepository.findById(id).map(m -> {this.invitationRepository.delete(m);
                    return m;})
                .orElseThrow(()-> new EntityNotFoundException(id, Invitation.class));
    }
}
