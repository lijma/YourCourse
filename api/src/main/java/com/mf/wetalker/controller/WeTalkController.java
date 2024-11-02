package com.mf.wetalker.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mf.wetalker.infra.auth.LogRequired;
import com.mf.wetalker.infra.auth.UserContextHolder;
import com.mf.wetalker.infra.db.entity.*;
import com.mf.wetalker.infra.json.MfObjectMapper;
import com.mf.wetalker.infra.tencent.CosProperty;
import com.mf.wetalker.model.*;
import com.mf.wetalker.service.ConfigService;
import com.mf.wetalker.service.TokenService;
import com.mf.wetalker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wetalk")
@RequiredArgsConstructor
@Slf4j
public class WeTalkController {
    private final TokenService tokenService;
    private final UserService userService;
    private final CosProperty config;
    private final ConfigService configService;

    @PostMapping("/login")
    @Validated
    public String Login(@Validated @RequestBody WeChatLoginRequest request) {
        String token = tokenService.login(request);
        return token;
    }

    @GetMapping("/profile")
    @LogRequired
    public WeTalkUser profile() {
        return tokenService.getProfile(UserContextHolder.getUserId());
    }

    @PostMapping("/profile/update")
    @LogRequired
    public void updateProfile(@RequestBody WeTalkUser weTalkUser) {
        weTalkUser.setId(UserContextHolder.getUserId());
        userService.updateProfile(weTalkUser);
    }

    @GetMapping("/ads")
    @Validated
    public List<AdInfo> ads() {
        return userService.validAds();
    }

    @PostMapping("/sites")
    @Validated
    @LogRequired
    public void applySite(@Validated @RequestBody Site site) {
        userService.applySite(site);
    }

    @GetMapping("/sites/{siteId}")
    @Validated
    public SiteAndActivities site(@PathVariable BigInteger siteId) {
        return userService.siteAndActivities(siteId);
    }

    @GetMapping("/sites")
    @Validated
    @LogRequired
    public List<Site> ownSites() {
        return userService.ownSites(UserContextHolder.getUserId());
    }

    @GetMapping("/sites/mine")
    @Validated
    @LogRequired
    public List<SiteAndMembers> mySites() {
        return userService.siteMembers();
    }

    @PostMapping("/sites/invite")
    @Validated
    @LogRequired
    public void joinSite(@Validated @RequestBody SiteInviteRequest site) {
        userService.joinSite(site);
    }

    @PostMapping("/sites/asRunner")
    @Validated
    @LogRequired
    public void asRunner(@Validated @RequestBody SiteRunnerRequest request) {
        userService.markAsRunner(request);

    }

    @PostMapping("/sites/deleteUser")
    @Validated
    @LogRequired
    public void deleteUserFromSite(@Validated @RequestBody SiteRunnerRequest request) {
        userService.deleteUser(request);
    }

    @PostMapping("/activities")
    @Validated
    @LogRequired
    public void createActivity(@Validated @RequestBody Activity activity) {
        userService.newActivity(activity);
    }

    @PostMapping("/activities/{activityId}")
    @Validated
    @LogRequired
    public ResponseEntity deleteActivity(@Validated @PathVariable("activityId") BigInteger activityId) {
        if (userService.delActivity(activityId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/activities/owned")
    @Validated
    @LogRequired
    public List<Activity> myCreatedActivities(@RequestParam(value = "page", required = false) Integer page) {
        return userService.myOwnedActivities(page);
    }

    @GetMapping("/activities/joined")
    @Validated
    @LogRequired
    public List<Activity> myJoinedActivities(@RequestParam(value = "page", required = false) Integer page) {
        return userService.joinedActivities(page);
    }


    @GetMapping("/activities/{activityId}")
    @Validated
    public ActivityDetail getActivityDetail(@Validated @PathVariable("activityId") BigInteger activityId) {
        return userService.activityDetail(activityId);
    }

    @PostMapping("/activities/join")
    @Validated
    @LogRequired
    public ResponseEntity<String> joinActivity(@Validated @RequestBody ActivityJoinRequest request) {
        userService.joinActivity(request);
        return ResponseEntity.ok().body("报名成功！");
    }

    @PostMapping("/activities/roles")
    @Validated
    @LogRequired
    public ResponseEntity updateActivityRoles(@Validated @RequestBody RoleRequest request) {
        userService.updateRoles(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activities/{activityId}/votes")
    @Validated
    @LogRequired
    public ResponseEntity updateVoteDetails(
            @PathVariable BigInteger activityId,
            @Validated @RequestBody Object voteObject) {
        String votes = MfObjectMapper.readObjectsAsString(voteObject);
        userService.updateVotes(activityId,votes);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activities/{activityId}/votes/results")
    @Validated
    @LogRequired
    public ResponseEntity vote(@PathVariable BigInteger activityId) {
        Map body = userService.voteResult(activityId);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/activities/{activityId}/vote")
    @Validated
    @LogRequired
    public ResponseEntity vote(
            @PathVariable BigInteger activityId,
            @Validated @RequestBody VoteRequest request) {

        userService.vote(activityId,request.toVotes(activityId));
        return ResponseEntity.ok().build();
    }



    @GetMapping("/activities/valid")
    @Validated
    public List<Activity> validActivities(@RequestParam(value = "page", required = false) Integer page) {
        return userService.validaActivity(page);
    }

    @PostMapping("/seach")
    @Validated
    public SitesAndActivities search(@NotNull @RequestBody SearchRequest request) {
        return userService.search(request.getSearch());
    }

    @PostMapping("/programs")
    @Validated
    @LogRequired
    public ProgramAndLabelsRequest create(@RequestBody ProgramAndLabelsRequest program) {
        return userService.createProgram(program);
    }

    @GetMapping("/programs/owned")
    @Validated
    @LogRequired
    public List<Program> myOwnedProgram() {
        return userService.myPrograms();
    }


    @PostMapping("/programs/{id}")
    @Validated
    @LogRequired
    public ResponseEntity deleteProgram(@Validated @PathVariable("id") BigInteger programId) {
        if (userService.delProgram(programId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/programs/{id}")
    public ProgramAndLabels programs(@PathVariable BigInteger id) {
        ProgramAndLabels programAndLabels = userService.programDetail(id);
        return programAndLabels;
    }

    @GetMapping("/programs/valid")
    @Validated
    public List<Program> validPrograms() {
        return userService.validPrograms();
    }

    @GetMapping("/programs/summary")
    @Validated
    public Summary programSummary() {
        return userService.summary();
    }


    @PostMapping("/programs/join")
    @Validated
    @LogRequired
    public ResponseEntity<String> joinActivity(@Validated @RequestBody ProgramJoinRequest request) {
        userService.joinProgram(request);
        return ResponseEntity.ok().body("报名成功！");
    }

    @GetMapping("/programs/joined")
    @Validated
    @LogRequired
    public List<UserAndProgram> myJoinedPrograms() {
        return userService.joinedPrograms();
    }

    @GetMapping("/programs/{id}/list")
    @LogRequired
    public ProgramAndUsers programUsers(@PathVariable BigInteger id) {
        ProgramAndUsers programAndUsers = userService.programList(id);
        return programAndUsers;
    }

    @PostMapping("/teachers")
    @Validated
    @LogRequired
    public void applyTeacher(@Validated @RequestBody Teacher teacher) {
        userService.applyTeacher(teacher);
    }

    @GetMapping("/teachers/valid")
    @LogRequired
    public Teacher approvedTeacher() {
        return userService.findApprovedTeacher(UserContextHolder.getUserId());
    }

    @GetMapping("/programs/{id}/teacher")
    public Teacher validTeacher(@PathVariable("id") BigInteger programId) {
        return userService.findValidTeacherByProgram(programId);
    }

    @GetMapping("/teachers/{id}")
    public Teacher findTeacherById(@PathVariable("id") BigInteger teacherId) {
        return userService.findTeacherById(teacherId);
    }

    @GetMapping("/teachers/{id}/programs")
    public List<Program> teacherPrograms(@PathVariable("id") BigInteger teacherId) {
        return userService.findValidProgramsByTeacher(teacherId);
    }

    @PostMapping("/favor")
    @Validated
    @LogRequired
    public void site(@Validated @RequestBody Favor favorRequest) {
        favorRequest.setUserId(UserContextHolder.getUserId());
        userService.favor(favorRequest);
    }

    @GetMapping("/favors/myfavor")
    @Validated
    @LogRequired
    public MyFavor site() {
        return userService.myFavors(UserContextHolder.getUserId());
    }

    @GetMapping("/cos-config")
    public CosConfig cosConfig() {
        return new CosConfig(config);
    }

    @GetMapping("/template-config")
    public JsonNode templateConfig() {
        return configService.loadTemplateConfig();
    }

    @GetMapping("/toggle-config")
    public JsonNode toggleConfig() {
        return configService.loadToggleConfig();
    }
}


