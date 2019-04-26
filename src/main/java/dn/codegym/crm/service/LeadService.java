package dn.codegym.crm.service;
import dn.codegym.crm.dto.LeadDTO;
import dn.codegym.crm.entity.Lead;

public interface LeadService {
    Iterable<Lead> findAllByDeletedIsFalse();
    Iterable<Lead> findAllByStatusContaining(String status);
    void create(LeadDTO leadDTO);
    void update(LeadDTO leadDTO);
    LeadDTO findById(String id);
    void delete(String id);
}