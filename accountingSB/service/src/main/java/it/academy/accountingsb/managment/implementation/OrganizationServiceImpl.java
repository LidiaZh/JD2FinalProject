package it.academy.accountingsb.managment.implementation;

import it.academy.accountingsb.dao.OrganizationRepository;
import it.academy.accountingsb.dto.OrganizationDto;
import it.academy.accountingsb.managment.interfaces.OrganizationService;
import it.academy.accountingsb.mappers.OrganizationMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository,
                                   OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {
        return organizationMapper.toOrganizationDto(organizationRepository.save(organizationMapper.toOrganization(organizationDto)));

    }

    @Override
    public void delOrganization(Integer idOrganization) {
        organizationRepository.deleteById(idOrganization);
    }

    @Override
    public OrganizationDto getOrganization(Integer idOrganization) {
        return organizationMapper.toOrganizationDto(organizationRepository.findById(idOrganization)
                .orElse(null));
    }

    @Override
    public List<OrganizationDto> getListOfOrganizationDto() {
        return organizationRepository.findAll().stream()
                .map(organizationMapper::toOrganizationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Page<OrganizationDto> organizationByPage(int pageSize,
                                                    int pageNum,
                                                    String sortField,
                                                    String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return organizationRepository.findAll(pageable)
                .map(organizationMapper::toOrganizationDto);
    }
}
