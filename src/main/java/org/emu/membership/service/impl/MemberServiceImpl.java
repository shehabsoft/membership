package org.emu.membership.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.emu.membership.domain.Member;
import org.emu.membership.repository.MemberRepository;
import org.emu.membership.repository.search.MemberSearchRepository;
import org.emu.membership.service.MemberService;
import org.emu.membership.service.dto.MemberDTO;
import org.emu.membership.service.mapper.MemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Member}.
 */
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    //    @Autowired
    //    private EsIndexRepository esIndexRepository;

    private List<String> allIndices = new ArrayList<>();
    private final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    private final MemberSearchRepository memberSearchRepository;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper, MemberSearchRepository memberSearchRepository) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.memberSearchRepository = memberSearchRepository;
    }

    @Override
    public MemberDTO save(MemberDTO memberDTO) {
        log.debug("Request to save Member : {}", memberDTO);
        Member member = memberMapper.toEntity(memberDTO);
        member = memberRepository.save(member);
        MemberDTO result = memberMapper.toDto(member);
        //  memberSearchRepository.save(member);
        return result;
    }

    @Override
    public Optional<MemberDTO> partialUpdate(MemberDTO memberDTO) {
        log.debug("Request to partially update Member : {}", memberDTO);

        return memberRepository
            .findById(memberDTO.getId())
            .map(
                existingMember -> {
                    if (memberDTO.getFirstName() != null) {
                        existingMember.setFirstName(memberDTO.getFirstName());
                    }

                    if (memberDTO.getLastName() != null) {
                        existingMember.setLastName(memberDTO.getLastName());
                    }

                    if (memberDTO.getCivilId() != null) {
                        existingMember.setCivilId(memberDTO.getCivilId());
                    }

                    if (memberDTO.getBirthDate() != null) {
                        existingMember.setBirthDate(memberDTO.getBirthDate());
                    }

                    if (memberDTO.getEmail() != null) {
                        existingMember.setEmail(memberDTO.getEmail());
                    }

                    if (memberDTO.getPhone() != null) {
                        existingMember.setPhone(memberDTO.getPhone());
                    }

                    if (memberDTO.getAddress1() != null) {
                        existingMember.setAddress1(memberDTO.getAddress1());
                    }

                    if (memberDTO.getAddress2() != null) {
                        existingMember.setAddress2(memberDTO.getAddress2());
                    }

                    if (memberDTO.getCity() != null) {
                        existingMember.setCity(memberDTO.getCity());
                    }

                    if (memberDTO.getCountry() != null) {
                        existingMember.setCountry(memberDTO.getCountry());
                    }

                    if (memberDTO.getSalary() != null) {
                        existingMember.setSalary(memberDTO.getSalary());
                    }
                    //
                    //                    if (memberDTO.getGender() != null) {
                    //                        existingMember.setGender(memberDTO.getGender());
                    //                    }

                    return existingMember;
                }
            )
            .map(memberRepository::save)
            .map(
                savedMember -> {
                    memberSearchRepository.save(savedMember);

                    return savedMember;
                }
            )
            .map(memberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Members");
        return memberRepository.findAll(pageable).map(memberMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberDTO> findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        return memberRepository.findById(id).map(memberMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        memberRepository.deleteById(id);
        memberSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MemberDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Members for query {}", query);
        return memberSearchRepository.search(queryStringQuery(query), pageable).map(memberMapper::toDto);
    }

    @Override
    public List<Member> searchByFirstName(String key) {
        return memberSearchRepository.findByFirstNameContaining(key);
    }

    /*
    public List<SearchHits<?>> searchInMultipleIdxs2(String query) throws ClassNotFoundException {

        //TODO: change findAll to search in specific Indices (not all Indices) based on config
        List<EsIndex> allIndices2 = esIndexRepository.findAll();

        List<Query> inputsQueries = new ArrayList<Query>(); // input 1 to call multiSearch method
        List<Class<?>> IndicesToSearch = new ArrayList<>(); // input 2 to call multiSearch method

        for (EsIndex esIndex : allIndices2) {   // loop on all indices
            Set<EsField> esFields = esIndex.getEsFields();
            // get all fields (names) of current index and store them in list of string
            List<String> namesList = esFields.stream().filter(current -> current.isEnabled()).map(current -> current.getFieldName()).collect(Collectors.toList());
            Object[] objArr = namesList.toArray();
            String[] arr = Arrays.copyOf(objArr, objArr.length, String[].class); // convert to array of strings to pass it to method
            QueryBuilder queryBuilder = QueryBuilders
                .multiMatchQuery(query, arr)
                .fuzziness(Fuzziness.AUTO);
            Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).build();
            inputsQueries.add(searchQuery);
            IndicesToSearch.add(Class.forName(esIndex.getFullQualifiedName())); //get current FullQualifiedName of index and collect it

        }
        List<SearchHits<?>> searchHits = elasticsearchOperations.multiSearch(inputsQueries, IndicesToSearch);
        return searchHits;
    }*/

    public List<SearchHits<?>> searchInMultipleIdxs(String query) throws ClassNotFoundException {
        allIndices.add("org.emu.membership.domain.Member");
        allIndices.add("org.emu.membership.domain.MembershipStatus");
        //allIndices.add("com.emu.rule_engine_ms.domain.StpMessage");
        System.out.println(
            "com.emu.rule_engine_ms.domain.StpMessagecom.emu.rule_engine_ms.domain.StpMessagecom.emu.rule_engine_ms.domain.StpMessage"
        );
        //TODO: make dynamic queryBuilders based on specific configurable fields
        QueryBuilder queryBuilder1 = QueryBuilders.wildcardQuery("firstName", query + "*");
        QueryBuilder queryBuilder2 = QueryBuilders.wildcardQuery("name", query + "*");
        // QueryBuilder queryBuilder3 = QueryBuilders.wildcardQuery("messageAr", query + "*");

        Query searchQuery1 = new NativeSearchQueryBuilder().withFilter(queryBuilder1).build();
        Query searchQuery2 = new NativeSearchQueryBuilder().withFilter(queryBuilder2).build();

        List<Query> inputs = new ArrayList<Query>();
        inputs.add(searchQuery1);
        inputs.add(searchQuery2);

        List<Class<?>> IndicesToSearch = new ArrayList<>();

        allIndices.forEach(
            indexName -> {
                try {
                    IndicesToSearch.add(Class.forName(indexName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        );
        //        List<SearchHits<?>> searchHits = elasticsearchOperations.multiSearch(inputs, IndicesToSearch);
        try {
            List<SearchHits<?>> searchHits = elasticsearchOperations.multiSearch(
                inputs,
                IndicesToSearch,
                IndexCoordinates.of("member", "membershipstatus", "stpmessage")
            );
            return searchHits;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
