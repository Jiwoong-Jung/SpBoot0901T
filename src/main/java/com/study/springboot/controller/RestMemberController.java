package com.study.springboot.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.springboot.spring.DuplicateMemberException;
import com.study.springboot.spring.Member;
import com.study.springboot.spring.MemberDao;
import com.study.springboot.spring.MemberRegisterService;
import com.study.springboot.spring.RegisterRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
//@Controller
@Slf4j
public class RestMemberController {
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private MemberRegisterService memberRegisterService;
	
	@GetMapping("/rest")
	public Integer rest1() {
		return memberDao.count();
	}
	
	@GetMapping("/api/members")
	//@ResponseBody
	public List<Member> members() {
		return memberDao.selectAll();
	}
	
	@PostMapping("/api/members")
	public void newMember(@RequestBody @Valid RegisterRequest regReq,
			HttpServletResponse response) throws IOException {
		log.info("================{}", regReq);
		try {
			Long newMemberId = memberRegisterService.regist(regReq);
			response.setHeader("Location", "/api/members/"+newMemberId);
			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch(DuplicateMemberException e) {
			response.sendError(HttpServletResponse.SC_CONFLICT);
		}
	}
	
	@GetMapping("/rest2")
	public void rest2(HttpServletResponse response) throws IOException {
		response.sendError(HttpServletResponse.SC_CONFLICT);
	}
	
	@GetMapping("/api/members/{id}")
	//@ResponseBody
	public Member member(@PathVariable Long id, 
			                HttpServletResponse response) 
			                		            throws IOException {
		Member member = memberDao.selectById(id);
		if (member == null) {
			response.sendError(Response.SC_NOT_FOUND);
		}
		return member;
	}

}
