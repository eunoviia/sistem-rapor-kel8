    package com.example.report.model;
    
    import jakarta.persistence.Column;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.Id;
    import jakarta.persistence.MappedSuperclass;
    
    @MappedSuperclass
    public abstract class User {
    
        @Id
        @GeneratedValue
        private Long id;
    
        @Column(nullable = false, unique = true)
        private String email;
    
        @Column(nullable = false)
        private String password;
    
        @Column(nullable = false)
        private String name;
    
        @Column(nullable = false)
        private String role;
    
        public abstract boolean login(String email, String password);
    
        // Getters and Setters
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getEmail() {
            return email;
        }
    
        public void setEmail(String email) {
            this.email = email;
        }
    
        public String getPassword() {
            return password;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getRole() {
            return role;
        }
    
        public void setRole(String role) {
            this.role = role;
        }
    }
