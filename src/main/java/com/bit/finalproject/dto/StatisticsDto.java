package com.bit.finalproject.dto;

public class StatisticsDto {
    private int totalUsers;
    private int newUsers;
    private int activeUsers;
    private String serverStatus;
    private double pageLoadTime;
    private String recentActivity;
    private String errorLogs;

    // Getters and Setters
    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(int newUsers) {
        this.newUsers = newUsers;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public double getPageLoadTime() {
        return pageLoadTime;
    }

    public void setPageLoadTime(double pageLoadTime) {
        this.pageLoadTime = pageLoadTime;
    }

    public String getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(String recentActivity) {
        this.recentActivity = recentActivity;
    }

    public String getErrorLogs() {
        return errorLogs;
    }

    public void setErrorLogs(String errorLogs) {
        this.errorLogs = errorLogs;
    }
}
