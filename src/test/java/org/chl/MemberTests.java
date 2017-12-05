package org.chl;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.chl.models.Member;
import org.chl.repos.FriendListRepository;
import org.chl.repos.MemberRepository;
import org.chl.services.MemberService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.assertj.core.api.Assertions.assertThat;;

/**
 * Created by ibrahim on 12/5/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MemberService.class)
@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepo;

    @MockBean
    private FriendListRepository friendListRepository;

    private static final String name = "ibrahim";
    private static final String surname = "akay";
    private static final String email = "that_see";
    private static final String id = "1234";

    @Before
    public void setup() {
        Member member = new Member();
        member.setId(id);
        member.setName(name);
        member.setSurname(surname);
        member.setEmail(email);
        Mockito.when(memberRepo.findByEmail(member.getEmail())).thenReturn(member);
        Mockito.when(memberRepo.findOne(member.getId())).thenReturn(member);
    }

    @Test
    public void verifyGetMember() throws  Exception{
        Member found = memberService.getMemberInfoByEmail(email);
        assertThat(found.getEmail()).isEqualTo(email);
        assertThat(found.getName()).isEqualTo(name);
        assertThat(found.getSurname()).isEqualTo(surname);
    }

    @Test
    public void verifyAddMember() throws  Exception{
        Member member = new Member();
        member.setId(id);
        member.setName(name);
        member.setSurname(surname);
        member.setEmail(email);
        memberService.addMember(member);
    }

    @Test
    public void verifyGetMemberInfo() throws  Exception{
        Member found = memberService.getMemberInfo(id);
        assertThat(found.getEmail()).isEqualTo(email);
        assertThat(found.getName()).isEqualTo(name);
        assertThat(found.getSurname()).isEqualTo(surname);
        // assertNotNull
    }
}
