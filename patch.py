import re

with open("src/main/java/com/cism/backend/service/system/chat/ChatService.java", "r") as f:
    content = f.read()

# Add import
content = content.replace("import jakarta.transaction.Transactional;", "import jakarta.transaction.Transactional;\nimport jakarta.servlet.http.HttpServletRequest;")

# Add autowired request
content = content.replace("@Autowired\n    private SimpMessagingTemplate messagingTemplate;", "@Autowired\n    private SimpMessagingTemplate messagingTemplate;\n\n    @Autowired\n    private HttpServletRequest request;\n\n    private boolean isStallAppRequest(String currentUsername) {\n        if (stallRepository.findByLicence(currentUsername).isEmpty()) return false;\n        try {\n            String appType = request.getHeader(\"X-App-Type\");\n            if (appType == null) appType = request.getParameter(\"appType\");\n            if (appType != null) return \"stall\".equalsIgnoreCase(appType);\n            return request.getRequestURI() != null && request.getRequestURI().contains(\"/stall\");\n        } catch (Exception e) {\n            return true;\n        }\n    }")

# Replace boolean isStallOwner = stall.getLicence().equals(currentUsername);
content = content.replace("boolean isStallOwner = stall.getLicence().equals(currentUsername);", "boolean isStallOwner = stall.getLicence().equals(currentUsername) && isStallAppRequest(currentUsername);")

# Replace boolean isOwner = stall.getLicence().equals(currentUsername);
content = content.replace("boolean isOwner = stall.getLicence().equals(currentUsername);", "boolean isOwner = stall.getLicence().equals(currentUsername) && isStallAppRequest(currentUsername);")

# Replace boolean isStallOwner = stallRepository.findByLicence(currentUsername).isPresent();
content = content.replace("boolean isStallOwner = stallRepository.findByLicence(currentUsername).isPresent();", "boolean isStallOwner = isStallAppRequest(currentUsername);")

with open("src/main/java/com/cism/backend/service/system/chat/ChatService.java", "w") as f:
    f.write(content)

print("Patched!")
