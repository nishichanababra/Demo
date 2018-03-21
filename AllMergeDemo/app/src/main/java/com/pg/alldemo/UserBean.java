package com.pg.alldemo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by test on 27/2/18.
 */

public class UserBean implements Serializable {


    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("header")
    private Header header;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public static class Data {
        @Expose
        @SerializedName("leaderboard")
        public List<Leaderboard> leaderboard;
        @Expose
        @SerializedName("user_data")
        private User_data user_data;

        public List<Leaderboard> getLeaderboard() {
            return leaderboard;
        }

        public void setLeaderboard(List<Leaderboard> leaderboard) {
            this.leaderboard = leaderboard;
        }

        public User_data getUser_data() {
            return user_data;
        }

        public void setUser_data(User_data user_data) {
            this.user_data = user_data;
        }
    }

    public static class Leaderboard {
        @Expose
        @SerializedName("profile_pic")
        private String profile_pic;
        @Expose
        @SerializedName("cumulative_points")
        private String cumulative_points;
        @Expose
        @SerializedName("branch_name")
        private String branch_name;
        @Expose
        @SerializedName("name")
        private String name;
        @Expose
        @SerializedName("member_id")
        private String member_id;
        @Expose
        @SerializedName("rank")
        private int rank;

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getCumulative_points() {
            return cumulative_points;
        }

        public void setCumulative_points(String cumulative_points) {
            this.cumulative_points = cumulative_points;
        }

        public String getBranch_name() {
            return branch_name;
        }

        public void setBranch_name(String branch_name) {
            this.branch_name = branch_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    public static class User_data {
        @Expose
        @SerializedName("total_month_points")
        private String total_month_points;
        @Expose
        @SerializedName("today_points")
        private String today_points;
        @Expose
        @SerializedName("today_rank")
        private String today_rank;

        public String getTotal_month_points() {
            return total_month_points;
        }

        public void setTotal_month_points(String total_month_points) {
            this.total_month_points = total_month_points;
        }

        public String getToday_points() {
            return today_points;
        }

        public void setToday_points(String today_points) {
            this.today_points = today_points;
        }

        public String getToday_rank() {
            return today_rank;
        }

        public void setToday_rank(String today_rank) {
            this.today_rank = today_rank;
        }
    }

    public static class Header {
        @Expose
        @SerializedName("message")
        private String message;
        @Expose
        @SerializedName("status")
        private int status;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
