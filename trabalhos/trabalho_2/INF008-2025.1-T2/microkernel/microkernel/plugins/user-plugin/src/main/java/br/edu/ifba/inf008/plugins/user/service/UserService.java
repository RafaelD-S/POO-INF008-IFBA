package br.edu.ifba.inf008.plugins.user.service;

import br.edu.ifba.inf008.plugins.user.dao.UserDAO;
import br.edu.ifba.inf008.plugins.user.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    public Optional<User> getUserById(int id) {
        return userDAO.findById(id);
    }
    
    public List<User> searchUsersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllUsers();
        }
        return userDAO.findByNameContaining(name);
    }
    
    public ValidationResult validateUser(User user, boolean isUpdate) {
        ValidationResult result = new ValidationResult();
        
        // Validate name
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            result.addError("Nome é obrigatório");
        } else if (user.getName().trim().length() < 2) {
            result.addError("Nome deve ter pelo menos 2 caracteres");
        } else if (user.getName().trim().length() > 100) {
            result.addError("Nome deve ter no máximo 100 caracteres");
        }
        
        // Validate email
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            result.addError("Email é obrigatório");
        } else if (!isValidEmail(user.getEmail().trim())) {
            result.addError("Email deve ter um formato válido");
        } else if (user.getEmail().trim().length() > 255) {
            result.addError("Email deve ter no máximo 255 caracteres");
        } else {
            // Check email uniqueness
            int excludeId = isUpdate ? user.getId() : -1;
            if (userDAO.emailExists(user.getEmail().trim(), excludeId)) {
                result.addError("Email já está em uso por outro usuário");
            }
        }
        
        return result;
    }
    
    public boolean createUser(User user) {
        ValidationResult validation = validateUser(user, false);
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Dados inválidos: " + validation.getErrorsAsString());
        }
        
        return userDAO.insert(user);
    }
    
    public boolean updateUser(User user) {
        ValidationResult validation = validateUser(user, true);
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Dados inválidos: " + validation.getErrorsAsString());
        }
        
        return userDAO.update(user);
    }
    
    public boolean deleteUser(int userId) {
        return userDAO.delete(userId);
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    public static class ValidationResult {
        private final List<String> errors;
        
        public ValidationResult() {
            this.errors = new java.util.ArrayList<>();
        }
        
        public void addError(String error) {
            errors.add(error);
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
        
        public List<String> getErrors() {
            return new java.util.ArrayList<>(errors);
        }
        
        public String getErrorsAsString() {
            return String.join(", ", errors);
        }
    }
}
