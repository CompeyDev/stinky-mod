package xyz.devcomp.config;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigModel {
        @JsonProperty("MOTDs")
        private ArrayList<String> MOTDs;
        @JsonProperty("joinMessages")
        private ArrayList<String> joinMessages;
        @JsonProperty("leaveMessages")
        private ArrayList<String> leaveMessages;

        public ConfigModel() {}

        public ArrayList<String> getMOTDs() {
            return this.MOTDs;
        }

        public ArrayList<String> getJoinMessageStrings() {
            return this.joinMessages;
        }

         public ArrayList<String> getLeaveMessageStrings() {
            return this.leaveMessages;
        }
}
