package org.emu.membership.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import java.time.Instant;
import java.util.Set;
import org.emu.common.dto.MemberDto;
import org.emu.common.status.MemberApprovalStatus;
import org.emu.common.status.NotificationStatus;
import org.emu.membership.IntegrationTest;
import org.emu.membership.domain.*;
import org.emu.membership.domain.enumeration.Gender;
import org.emu.membership.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@IntegrationTest
@AutoConfigureMockMvc
@EnableAutoConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class Tes {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    //    @Test
    //    public void getMessageTest() throws Exception {
    //        mockMvc.perform(MockMvcRequestBuilders.get("/api/m")).andExpect(status().isOk());
    //    }

    @Test
    @WithMockUser(username = "shehab", password = "1234", roles = "ADMIN")
    public void getMembersTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/members")).andDo(print()).andReturn();
        String actualJson = result.getResponse().getContentAsString();
        MemberDto[] memberDto = objectMapper.readValue(actualJson, MemberDto[].class);
        System.out.println("actualJson:" + actualJson);
        System.out.println("memberDtos:" + memberDto.length);
    }

    @Test
    @WithMockUser(username = "shehab", password = "1234", roles = "ADMIN")
    public void getMemberByIdTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/members/{id}", 2l)).andDo(print()).andReturn();
        String actualJson = result.getResponse().getContentAsString();
        MemberDto memberDto = objectMapper.readValue(actualJson, MemberDto.class);
        System.out.println("actualJson:" + actualJson);
        System.out.println("memberDto:" + memberDto);
        System.out.println("firstname:" + memberDto.getFirstName());
    }

    @Test
    @WithMockUser(username = "shehab", password = "1234", roles = "ADMIN")
    public void createMember() throws Exception {
        Member member = new Member();
        member.setAddress1("setAddress121222");
        member.setAddress2("setAddress2");
        member.setCity("cityaaaa");
        member.setCivilId("aaaa");
        member.setFirstName("firstaa");
        member.setLastName("lasaat");
        member.setCountry("country");
        member.setEmail("maaaail@mail.com");
        //    member.setGender(Gender.FEMALE);
        member.setBirthDate(Instant.now());
        member.setPhone("+02554444444");
        //    member.setMemberApprovalStatus(MemberApprovalStatus.APPROVED);
        //    member.setNotificationStatus(NotificationStatus.NEW);
        MembershipCategory membershipCategory = new MembershipCategory();
        membershipCategory.setDescription("aaaa");
        membershipCategory.setId(1l);
        member.setMembershipCategory(membershipCategory);
        MembershipType membershipType = new MembershipType();
        membershipType.setDescription("aaa");
        membershipType.setId(1l);
        member.setMembershipType(membershipType);
        MembershipLevel membershipLevel = new MembershipLevel();
        membershipLevel.setDescription("aaaa");
        membershipLevel.setId(1l);
        member.setMembershipLevel(membershipLevel);
        MembershipStatus membershipStatus = new MembershipStatus();
        membershipStatus.setDescription("aaa");
        membershipStatus.setId(1l);
        member.setMembershipStatus(membershipStatus);

        ObjectMapper mapper = new ObjectMapper();
        Set<Object> registeredModuleIds = mapper.getRegisteredModuleIds();
        mapper.registerModule(new JSR310Module());
        registeredModuleIds = mapper.getRegisteredModuleIds();

        //        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(member);

        MvcResult result = mockMvc
            .perform(MockMvcRequestBuilders.post("/api/members").contentType("application/json").content(requestJson))
            .andReturn();
        String actualJson = result.getResponse().getContentAsString();
        System.out.println("actualJson:" + actualJson);
    }

    @Test
    @WithMockUser(username = "shehab", password = "1234", roles = "ADMIN")
    public void getMember() throws Exception {
        mockMvc.perform(get("/api/members/{id}", 500l)).andExpect(status().isNotFound());
    }
    //    @Test
    //    public void getMessageTest() throws Exception {
    //        assertEquals(1, 1);
    //    }
}
