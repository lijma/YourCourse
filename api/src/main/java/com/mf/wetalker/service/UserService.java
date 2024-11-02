package com.mf.wetalker.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mf.wetalker.infra.auth.UserContextHolder;
import com.mf.wetalker.infra.db.entity.*;
import com.mf.wetalker.infra.db.mapper.*;
import com.mf.wetalker.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    public static final String REVIEWING_SITE_STATUS = "REVIEWING";
    public static final String AUDIENCE_TAG = "AUD";
    public static final String ROLE_TAG = "ROLES";
    public static final int PAGE_SIZE = 10;
    private final SiteMapper siteMapper;
    private final UserSiteMapper userSiteMapper;
    private final ActivityMapper activityMapper;
    private final UserActivityMapper userActivityMapper;
    private final FavorMapper favorMapper;
    private final ProgramMapper programMapper;
    private final LabelMapper labelMapper;
    private final UserProgramMapper userProgramMapper;

    private final TeacherMapper teacherMapper;

    private final AdsMapper adsMapper;

    private final UserMapper userMapper;
    private final VoteMapper voteMapper;


    public void login(WeChatLoginRequest request) {

    }

    @NotNull
    public List siteMembers() {
        BigInteger userId = UserContextHolder.getUserId();
        List<UserSite> userSites = userSiteMapper.selectList(Wrappers.<UserSite>lambdaQuery()
                .eq(UserSite::getStatus, ApproveStatus.APPROVED.name())
                .eq(UserSite::getUserId, userId));

        if (!userSites.isEmpty()) {
            log.info("[wetalk] found sites: {} for user {}", userSites.size(), userId);
            Map<Boolean, List<UserSite>> siteGroup = userSites.stream().collect(groupingBy(UserSite::getAsRunner));

            return List.of(
                    siteGroup.get(false) != null ? getSiteAndMembers(siteGroup.get(false)) : Collections.emptyList(),
                    siteGroup.get(true) != null ? getSiteAndMembers(siteGroup.get(true)) : Collections.emptyList()
            );
        }

        return Collections.EMPTY_LIST;
    }

    @NotNull
    private List<SiteAndMembers> getSiteAndMembers(List<UserSite> item) {
        List<BigInteger> idList = item.stream().map(UserSite::getSiteId).toList();
        if (idList == null || idList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Site> sites = siteMapper.selectBatchIds(idList);
        if (sites == null || sites.isEmpty()) {
            return Collections.emptyList();
        }

        return sites.stream().map(site -> {
            SiteAndMembers siteAndMembers = SiteAndMembers.builder()
                    .site(site)
                    .siteUsers(getSiteUsers(site.getId(), null))
                    .build();
            return siteAndMembers;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void applySite(Site site) {
        siteMapper.insert(site);
        userSiteMapper.insert(UserSite.builder().siteId(site.getId()).userId(UserContextHolder.getUserId()).asRunner(true).status(REVIEWING_SITE_STATUS).build());


    }

    @Transactional
    public void joinSite(SiteInviteRequest site) {
        List<UserSite> userSites = userSiteMapper.selectList(Wrappers.<UserSite>lambdaQuery().eq(UserSite::getSiteId, site.getSiteId()).eq(UserSite::getUserId, UserContextHolder.getUserId()));
        if (!userSites.isEmpty()) {
            return;
        }
        userSiteMapper.insert(UserSite.builder().siteId(site.getSiteId()).userName(site.getUserName()).userId(UserContextHolder.getUserId()).status(ApproveStatus.APPROVED.name()).asRunner(false).build());


    }

    @Transactional
    public void markAsRunner(SiteRunnerRequest request) {
        List<UserSite> userSites = getSiteUsers(request.getSiteId(), request.getUserId());

        if (userSites.isEmpty()) {
            log.error("failed to assign runner to user: {}, due to user not exist", request.getUserId());
            return;
        }

        UserSite userSite = userSites.get(0);
        userSite.setAsRunner(true);
        userSiteMapper.updateById(userSite);
    }

    @Transactional
    public void deleteUser(SiteRunnerRequest request) {
        List<UserSite> userSites = userSiteMapper.selectList(getSiteUserQuery(request.getSiteId(), request.getUserId()));

        if (userSites.isEmpty()) {
            log.error("failed to assign runner to user: {}, due to user not exist", request.getUserId());
            return;
        }

        UserSite userSite = userSites.get(0);
        userSite.setDeleted(true);
        userSiteMapper.updateById(userSite);
    }

    @Transactional
    public void newActivity(Activity activity) {
        activity.setUserId(UserContextHolder.getUserId());
        if (activity.getId() != null) {
            activityMapper.updateById(activity);
        } else {
            activityMapper.insert(activity);
        }
    }


    @NotNull
    @Transactional
    public boolean delActivity(BigInteger activityId) {
        Activity activity = activityMapper.selectById(activityId);
        if (activity.getUserId().compareTo(UserContextHolder.getUserId()) != 0) {
            return false;
        }

        userActivityMapper.delete(Wrappers.<UserActivity>lambdaQuery().eq(UserActivity::getActivityId, activityId));
        activityMapper.deleteById(activityId);
        return true;
    }


    public List<Activity> myOwnedActivities(Integer currentPage) {
        Page<Activity> page = new Page<>(getCurrent(currentPage), PAGE_SIZE);

        LambdaQueryWrapper<Activity> eq = Wrappers.<Activity>lambdaQuery()
                .eq(Activity::getDeleted, false)
                .eq(Activity::getUserId, UserContextHolder.getUserId());
        IPage<Activity> activityPage = activityMapper.selectPage(page, eq);
        return activityPage.getRecords();
    }

    public List<Activity> joinedActivities(Integer currentPage) {

        Page<UserActivity> page = new Page<>(getCurrent(currentPage), PAGE_SIZE);

        BigInteger userId = UserContextHolder.getUserId();
        Page<UserActivity> userActivityPage = userActivityMapper.selectPage(page, Wrappers.<UserActivity>lambdaQuery()
                .eq(UserActivity::getUserId, userId));

        if (userActivityPage.getRecords().size() == 0) {
            log.warn("cannot find activity for user: {}", userId);
            return Collections.emptyList();
        }

        return activityMapper
                .selectBatchIds(userActivityPage.getRecords().stream().
                        map(UserActivity::getActivityId).collect(Collectors.toList()))
                .stream()
                .filter(a -> !a.getDeleted())
                .collect(Collectors.toList());
    }

    private static int getCurrent(Integer currentPage) {
        return currentPage != null ? currentPage : 1;
    }

    public ActivityDetail activityDetail(BigInteger activityId) {
        Map<String, List<UserActivity>> roleMap = new HashMap<>();
        Activity activity = activityMapper.selectById(activityId);
        List<UserActivity> roles = userActivityMapper.
                selectList(Wrappers.<UserActivity>lambdaQuery()
                        .eq(UserActivity::getActivityId, activityId)
                        .orderBy(true, true, UserActivity::getId));
        if (roles != null || roles.size() > 0) {
            roleMap = roles.stream().collect(groupingBy(e -> AUDIENCE_TAG.equalsIgnoreCase(e.getRole()) ?
                    AUDIENCE_TAG : ROLE_TAG));
        }

        ActivityDetail activityDetail = ActivityDetail.builder()
                .activity(activity)
                .roles(roleMap.getOrDefault(ROLE_TAG, Collections.emptyList()))
                .audiences(roleMap.getOrDefault(AUDIENCE_TAG, Collections.emptyList()))
                .build();

        return activityDetail;
    }

    @Transactional
    @NotNull
    public void joinActivity(ActivityJoinRequest request) {

        UserActivity activity = userActivityMapper.selectById(request.getRoleId());
        if (activity != null) {
            activity.setUserId(UserContextHolder.getUserId());
            activity.setUserName(request.getUserName());
            activity.setEmail(request.getEmail());
            userActivityMapper.updateById(activity);
        } else {
            joinAsAudience(request);
        }

    }

    private void joinAsAudience(ActivityJoinRequest request) {
        UserActivity activity;
        UserActivity userActivity = userActivityMapper
                .selectOne(Wrappers.<UserActivity>lambdaQuery()
                        .eq(UserActivity::getUserId, UserContextHolder.getUserId())
                        .eq(UserActivity::getActivityId, request.getActivityId())
                        .eq(UserActivity::getRole, AUDIENCE_TAG));
        activity = new UserActivity();
        activity.setUserId(UserContextHolder.getUserId());
        activity.setUserName(request.getUserName());
        activity.setEmail(request.getEmail());
        activity.setActivityId(request.getActivityId());
        activity.setRole(AUDIENCE_TAG);
        activity.setRoleText("观众");

        if (userActivity != null) {
            activity.setId(userActivity.getId());
            userActivityMapper.updateById(activity);
            return;
        }

        userActivityMapper.insert(activity);
    }

    public List<Activity> validaActivity(Integer currentPage) {
        Page<Activity> page = new Page<>(getCurrent(currentPage), PAGE_SIZE);

        Page<Activity> activityPage = activityMapper.selectPage(page,
                Wrappers.<Activity>lambdaQuery().ge(Activity::getStartAt, LocalDate.now().toDate())
                        .orderBy(true, true, Activity::getStartAt)
        );

        return activityPage.getRecords();
    }

    private List<UserSite> getSiteUsers(BigInteger siteId, BigInteger userId) {
        List<UserSite> userSites = userSiteMapper.selectList(getSiteUserQuery(siteId, userId).eq(UserSite::getDeleted, false));
        return userSites;
    }

    private LambdaQueryWrapper<UserSite> getSiteUserQuery(BigInteger siteId, BigInteger userId) {
        return Wrappers.<UserSite>lambdaQuery().eq(siteId != null, UserSite::getSiteId, siteId).eq(userId != null, UserSite::getUserId, userId);
    }


    @Transactional
    public void updateRoles(RoleRequest request) {
        List<UserActivity> userActivities = request.toUserActivities();
        log.info("total {} activity roles to be added,", userActivities.size());

        if (userActivities.isEmpty()) {
            return;
        }

        userActivityMapper.delete(Wrappers.<UserActivity>lambdaQuery()
                .eq(UserActivity::getActivityId, request.getActivityId()));
        userActivities.forEach(userActivity -> userActivityMapper.insert(userActivity));
    }

    public SiteAndActivities siteAndActivities(BigInteger siteId) {
        Site site = siteMapper.selectById(siteId);
        if (site == null) {
            return null;
        }

        Page<Activity> page = new Page<>(1, 10);
        LambdaQueryWrapper<Activity> queryWrapper = Wrappers.<Activity>lambdaQuery()
                .ge(Activity::getStartAt, LocalDate.now().toDate())
                .eq(Activity::getSiteId, siteId);
        List<Activity> activities = activityMapper.selectPage(page, queryWrapper).getRecords();

        return SiteAndActivities.builder()
                .site(site)
                .activities(activities)
                .build();
    }

    @Transactional
    public void favor(Favor favorRequest) {
        Favor favor = favorMapper.selectOne(Wrappers.<Favor>lambdaQuery()
                .eq(Favor::getUserId, favorRequest.getUserId())
                .eq(Favor::getRefId, favorRequest.getRefId()));
        if (favor != null) {
            favorRequest.setId(favor.getId());
            favorMapper.updateById(favorRequest);
            return;
        }
        favorMapper.insert(favorRequest);

    }

    public SitesAndActivities search(String content) {

        List<Site> sites = siteMapper.selectName(content);
        List<Activity> activities = activityMapper.selectName(content);

        return SitesAndActivities.builder()
                .sites(sites)
                .activities(activities)
                .build();
    }

    @Transactional
    public ProgramAndLabelsRequest createProgram(ProgramAndLabelsRequest programAndLabels) {
        Program program = programAndLabels.getProgram();
        programMapper.insert(program);

        programAndLabels.setId(program.getId());
        for (LabelRequest labelRequest : programAndLabels.getLabels()) {
            Label label = Label.builder()
                    .labelType(LabelType.PROGRAM.name())
                    .refId(program.getId())
                    .label(labelRequest.getTitle())
                    .build();
            labelMapper.insert(label);
        }
        return programAndLabels;
    }

    public List<Program> myPrograms() {
        return programMapper.selectList(Wrappers.
                <Program>lambdaQuery()
                .eq(Program::getDeleted, false)
                .eq(Program::getUserId, UserContextHolder.getUserId()));
    }

    @Transactional
    public boolean delProgram(BigInteger programId) {
        Program program = programMapper.selectById(programId);
        program.setDeleted(true);
        programMapper.updateById(program);
        return true;
    }

    public ProgramAndLabels programDetail(BigInteger id) {
        Program program = programMapper.selectById(id);
        List<Label> labels = labelMapper.selectList(Wrappers.<Label>lambdaQuery()
                .eq(Label::getLabelType, LabelType.PROGRAM.name())
                .eq(Label::getRefId, id)
        );
        ProgramAndLabels from = ProgramAndLabels.from(program);
        List<String> collect = labels.stream().map(e -> e.getLabel()).collect(Collectors.toList());
        from.setLabels(collect);
        return from;
    }

    public List<Program> validPrograms() {
        Page<Program> page = new Page<>(1, 30);
        LambdaQueryWrapper<Program> queryWrapper = Wrappers.<Program>lambdaQuery()
                .eq(Program::getDeleted, false)
                .gt(Program::getStopAt, LocalDate.now().toDate())
                .orderBy(true, true, Program::getStartAt);
        return programMapper.selectPage(page, queryWrapper).getRecords();
    }

    public void joinProgram(ProgramJoinRequest request) {
        UserProgram userProgram = request.toProgram();
        userProgramMapper.insert(userProgram);
    }

    public List<UserAndProgram> joinedPrograms() {
        BigInteger userId = UserContextHolder.getUserId();
        List<UserProgram> programs = userProgramMapper.selectList(Wrappers.<UserProgram>lambdaQuery()
                .eq(UserProgram::getUserId, userId));

        if (programs.isEmpty()) {
            log.warn("cannot find activity for user: {}", userId);
            return Collections.emptyList();
        }

        return programs.stream().map(program -> {
            Program program1 = programMapper.selectById(program.getProgramId());
            return UserAndProgram.builder()
                    .email(program.getEmail())
                    .userName(program.getUserName())
                    .program(program1)
                    .build();
        }).collect(Collectors.toList());
    }

    public ProgramAndUsers programList(BigInteger id) {
        Program program = programMapper.selectById(id);
        List<UserProgram> userPrograms = userProgramMapper
                .selectList(Wrappers.<UserProgram>lambdaQuery().eq(UserProgram::getProgramId, id));
        return ProgramAndUsers.builder()
                .program(program)
                .list(userPrograms)
                .build();
    }

    public Summary summary() {
        Long programCount = programMapper.selectCount(Wrappers.<Program>lambdaQuery()
                .eq(Program::getDeleted, false));
        Long userCount = userProgramMapper.selectCount(Wrappers.<UserProgram>lambdaQuery());

        return Summary.builder()
                .programCount(programCount)
                .userCount(userCount)
                .build();
    }

    public void applyTeacher(Teacher teacher) {

        BigInteger userId = UserContextHolder.getUserId();
        Teacher selectOne = teacherMapper.selectOne(Wrappers.<Teacher>lambdaQuery().eq(Teacher::getUserId, userId));
        teacher.setStatus(ApproveStatus.APPLYING.name());
        teacher.setUserId(userId);

        if (selectOne != null) {
            log.warn("{} already applied, no need applied again");
            teacher.setId(selectOne.getId());
            teacherMapper.updateById(teacher);
            return;
        }

        teacherMapper.insert(teacher);
    }

    public Teacher findApprovedTeacher(BigInteger userId) {
        return teacherMapper.selectOne(
                Wrappers.<Teacher>lambdaQuery()
                        .eq(Teacher::getUserId, userId)
                        .eq(Teacher::getStatus, ApproveStatus.APPROVED)
        );
    }

    public List<Program> findValidProgramsByTeacher(BigInteger teacherId) {

        Teacher teacher = teacherMapper.selectOne(
                Wrappers.<Teacher>lambdaQuery()
                        .eq(Teacher::getId, teacherId)
                        .eq(Teacher::getStatus, ApproveStatus.APPROVED)
        );
        if (teacher == null) {
            return Collections.emptyList();
        }

        Page<Program> page = new Page<>(1, 30);
        LambdaQueryWrapper<Program> queryWrapper = Wrappers.<Program>lambdaQuery()
                .eq(Program::getDeleted, false)
                .gt(Program::getStopAt, LocalDate.now().toDate())
                .eq(Program::getUserId, teacher.getUserId())
                .orderBy(true, true, Program::getStartAt);
        return programMapper.selectPage(page, queryWrapper).getRecords();

    }

    public Teacher findValidTeacherByProgram(BigInteger programId) {
        Program program = programMapper.selectById(programId);
        if (program == null) {
            return null;
        }
        return teacherMapper.selectOne(
                Wrappers.<Teacher>lambdaQuery()
                        .eq(Teacher::getUserId, program.getUserId())
                        .eq(Teacher::getStatus, ApproveStatus.APPROVED)
        );
    }

    public Teacher findTeacherById(BigInteger teacherId) {
        return teacherMapper.selectOne(
                Wrappers.<Teacher>lambdaQuery()
                        .eq(Teacher::getId, teacherId)
                        .eq(Teacher::getStatus, ApproveStatus.APPROVED)
        );
    }

    public MyFavor myFavors(BigInteger userId) {
        List<Favor> favors = favorMapper.selectList(Wrappers.<Favor>lambdaQuery()
                .eq(Favor::getUserId, userId)
        );
        if (favors == null || favors.isEmpty()) {
            return null;
        }
        Map<String, List<Favor>> favorMap = favors.stream().collect(groupingBy(Favor::getFavorType));
        return MyFavor.builder()
                .favors(favorMap)
                .build();
    }

    public List<AdInfo> validAds() {
        return adsMapper.selectList(Wrappers.<AdInfo>lambdaQuery()
                .eq(AdInfo::getStatus, AdStatus.ONLINE));
    }

    public List<Site> ownSites(BigInteger userId) {
        List<UserSite> userSites = userSiteMapper.selectList(Wrappers.<UserSite>lambdaQuery()
                .eq(UserSite::getStatus, ApproveStatus.APPROVED.name())
                .eq(UserSite::getUserId, userId)
                .eq(UserSite::getAsRunner, true));
        if (userSites == null || userSites.isEmpty()) {
            return Collections.emptyList();
        }
        return siteMapper.selectBatchIds(userSites.stream().map(UserSite::getSiteId).toList());
    }

    public void updateProfile(WeTalkUser weTalkUser) {
        userMapper.updateById(weTalkUser);
    }

    public void updateVotes(BigInteger activityId, String votes) {
        Activity activity = activityMapper.selectById(activityId);
        activity.setVotes(votes);
        activityMapper.updateById(activity);
    }

    public void vote(BigInteger activityId, List<Vote> votes) {
        List<Vote> voteList = voteMapper.selectList(Wrappers.<Vote>lambdaQuery()
                .eq(Vote::getActivityId, activityId)
                .eq(Vote::getUserId, UserContextHolder.getUserId()));
        if (voteList != null && voteList.size() >= 1) {
            log.error("用户已投票，无效。。。", votes);
            return;
        }
        votes.forEach(vote -> voteMapper.insert(vote));
    }

    public Map voteResult(BigInteger activityId) {
        Map<String, Map> voteResult = new HashMap<>();
        List<Vote> voteList = voteMapper.selectList(Wrappers.<Vote>lambdaQuery()
                .eq(Vote::getActivityId, activityId));

        Map<String, List<Vote>> questions = voteList.stream()
                .collect(groupingBy(Vote::getQuestion));

        questions.forEach((question, answers) -> {
            Map<String, Long> counts = answers.stream()
                    .collect(groupingBy(Vote::getAnswer, Collectors.counting()));
            answers.stream().collect(groupingBy(Vote::getAnswer, Collectors.counting()));

            voteResult.put(question, counts);

        });

        return voteResult;
    }

    static <T> java.util.function.Predicate<T>
    distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        java.util.Map<Object, Boolean> seen = new java.util.HashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


}
