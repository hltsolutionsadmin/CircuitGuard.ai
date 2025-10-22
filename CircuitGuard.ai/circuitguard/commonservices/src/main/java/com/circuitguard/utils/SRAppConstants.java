package com.circuitguard.utils;

/**
 * Global constants for the application.
 * Use SRAppConstants to keep all fixed values in one place.
 */
public interface SRAppConstants {

    // =======================================
    // General Constants
    // =======================================
    String _0 = "0";
    String _20 = "20";
    String OR = "or";
    String MULTIBLOCK = "MULTIBLOCK";
    String SINGLE = "SINGLE";
    String BEARER = "Bearer ";
    String DASHBOARD = "mediaType";

    // =======================================
    // Pagination / Response Metadata
    // =======================================
    String TOTAL_PAGES = "totalPages";
    String TOTAL_ITEMS = "totalItems";
    String CURRENT_PAGE = "currentPage";
    String PAGE_NUM = "pageNo";
    String PAGE_SIZE = "pageSize";
    String PROFILES = "data";

    // =======================================
    // Roles (Spring Security Expressions)
    // =======================================
    String ROLE_USER = "hasRole('ROLE_USER')";
    String ROLE_STUDENT = "ROLE_STUDENT";
    String ROLE_USER_ADMIN = "hasRole('ROLE_USER_ADMIN')";
    String ROLE_ROLE_BUILDER = "hasRole('ROLE_BUILDER')";
    String ROLE_LAND_OWNER = "hasRole('ROLE_LAND_OWNER')";
    String ROLE_SUPER_ADMIN = "hasRole('ROLE_SUPER_ADMIN')";
    String ROLE_HOSPITAL_ADMIN = "hasRole('ROLE_HOSPITAL_ADMIN')";
    String ROLE_DOCTOR = "hasRole('ROLE_DOCTOR')";
    String ROLE_RECEPTIONIST = "hasRole('ROLE_RECEPTIONIST')";
    String ROLE_HOSPITAL_MANAGER = "hasRole('ROLE_HOSPITAL_MANAGER')";
    String ROLE_TELECALLER = "hasRole('ROLE_TELECALLER')";
    String ROLE_CALL_SUPERVISOR = "hasRole('ROLE_CALL_SUPERVISOR')";
    String SKILL_SAVED_SUCCESS = "Skill saved successfully";
    String SKILL_FETCH_SUCCESS = "Skill fetched successfully";
    public static final String SKILL_ADDED_SUCCESS = "Skill added/assigned successfully";
    public static final String SKILL_REMOVED_SUCCESS = "Skill removed successfully";
    public static final String SKILL_DELETED_SUCCESS = "Skill deleted successfully";
    String EDUCATION = "EDUCATION";
    String INTERNSHIP = "INTERNSHIP";
    String JOB = "JOB";

    // =======================================
    // Direct Role Names (Without hasRole())
    // =======================================
    String ROLE_BUILDER = "ROLE_BUILDER";
    String ROLE_USER_ADMINS = "ROLE_USER_ADMIN";

    // =======================================
    // Statuses
    // =======================================
    String VERIFIED = "Verified";
    String FAILED = "FAILED";
    String SUCCESS = "Success";
    String PENDING = "PENDING";
    String REFUNDED = "REFUNDED";
    String PROCESSING = "PROCESSING";

    // =======================================
    // Application Messages (Optional)
    // You can add global success/failure messages here
    // =======================================
    String REQUIREMENT_CREATE_SUCCESS = "Requirement created successfully";
    String REQUIREMENT_FETCH_SUCCESS = "Requirement fetched successfully";
    String REQUIREMENT_LIST_SUCCESS = "Requirements listed successfully";
    String REQUIREMENT_UPDATE_SUCCESS = "Requirement updated successfully";
    String REQUIREMENT_DELETE_SUCCESS = "Requirement deleted successfully";

    String APPLICATION_CREATE_SUCCESS = "Application created successfully";
    String APPLICATION_FETCH_SUCCESS = "Application fetched successfully";
    String APPLICATION_LIST_SUCCESS = "Applications listed successfully";
    String APPLICATION_UPDATE_SUCCESS = "Application updated successfully";
    String APPLICATION_DELETE_SUCCESS = "Application deleted successfully";

    String REWARD_POINTS_ADD_SUCCESS    = "Reward points added successfully";
    String REWARD_POINTS_DEDUCT_SUCCESS = "Reward points deducted successfully";
    String REWARD_POINTS_TOTAL_SUCCESS  = "Total reward points fetched successfully";
    String REWARD_TXN_SAVE_SUCCESS      = "Reward transaction saved successfully";

    // =======================================
    // Experience Messages
    // =======================================
    String EXPERIENCE_CREATE_SUCCESS = "Experience added successfully";
    String EXPERIENCE_FETCH_SUCCESS  = "Experience fetched successfully";

    // =======================================
    // Education Messages
    // =======================================
    String EDUCATION_CREATE_SUCCESS = "Education added successfully";
    String EDUCATION_FETCH_SUCCESS  = "Education details fetched successfully";
    String EDUCATION_LIST_SUCCESS   = "Education details listed successfully";
    String EDUCATION_UPDATE_SUCCESS = "Education updated successfully";
    String EDUCATION_DELETE_SUCCESS = "Education deleted successfully";

    // =======================================
    // Internship Messages
    // =======================================
    String INTERNSHIP_CREATE_SUCCESS = "Internship added successfully";
    String INTERNSHIP_FETCH_SUCCESS  = "Internship details fetched successfully";
    String INTERNSHIP_LIST_SUCCESS   = "Internships listed successfully";
    String INTERNSHIP_UPDATE_SUCCESS = "Internship updated successfully";
    String INTERNSHIP_DELETE_SUCCESS = "Internship deleted successfully";

    // =======================================
    // Job Messages
    // =======================================
    String JOB_CREATE_SUCCESS = "Job added successfully";
    String JOB_FETCH_SUCCESS  = "Job details fetched successfully";
    String JOB_LIST_SUCCESS   = "Jobs listed successfully";
    String JOB_UPDATE_SUCCESS = "Job updated successfully";
    String JOB_DELETE_SUCCESS = "Job deleted successfully";




}


