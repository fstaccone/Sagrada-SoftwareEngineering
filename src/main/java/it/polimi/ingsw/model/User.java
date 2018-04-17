package it.polimi.ingsw.model;

import java.io.Serializable;

    public class User implements Serializable {
        private final String username;
        //private final String colour;

        public User(String username/*, String colour*/) {
            super();
            this.username = username;
            //this.colour = colour;
        }

        public String getUsername() {
            return username;
        }
    }
