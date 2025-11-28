package rafikibora.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import rafikibora.model.users.User;
@Data
@Schema(name = "SystemUser", description = "Simplified view of a system user returned in lists and lookups.")
public class SystemUser {
        @Schema(description = "User's first name", example = "Jane")
        private String firstName;
        @Schema(description = "User's last name", example = "Doe")
        private String lastName;
        @Schema(description = "User's email address", example = "jane.doe@example.com")
        private String email;
        @Schema(description = "User's phone number", example = "0712345678")
        private String phoneNo;
        @Schema(description = "Email of the user who created this user", example = "admin@example.com")
        private String createdBy;
        @Schema(description = "Email of the user who approved this user if approved", example = "checker@example.com")
        private String approvedBy;
        @Schema(description = "Unique system identifier of the user", example = "42")
        private String userid;
        @Schema(description = "Username used for login (often equals email)", example = "jane.doe@example.com")
        private String username;
        @Schema(description = "Merchant Identifier (MID) if the user is a merchant", example = "MID0011223344")
        private String mid;
        @Schema(description = "Business name if the user represents a business", example = "Acme Stores")
        private String businessName;
        @Schema(description = "Date the user was created", example = "2025-01-01 12:00:00")
        private String dateCreated;
        @Schema(description = "Whether the user is active", example = "true")
        private String status;
        @Schema(description = "Whether the user is marked as deleted", example = "false")
        private String deleted;

        public SystemUser(User user){
                this.firstName = user.getFirstName();
                this.lastName = user.getLastName();
                this.email = user.getEmail();
                this.dateCreated = String.valueOf(user.getDateCreated());
                this.phoneNo = user.getPhoneNo();
                this.status= String.valueOf(user.isStatus());
                this.deleted = String.valueOf(user.isDeleted());

                if(user.getUserMaker() != null)
                        this.createdBy = user.getUserMaker().getEmail();
                else this.createdBy = "SYSTEM ADMIN";

                if(user.getUserChecker() != null)
                        this.approvedBy = user.getUserChecker().getEmail();
                else
                        this.approvedBy = "UNAPPROVED";

                this.userid = String.valueOf(user.getUserid());
                this.username = user.getUsername();

                if(user.getMid() != null)
                        this.mid = user.getMid();
                else
                        this.mid = "NOT A MERCHANT";

                if(user.getBusinessName() != null)
                        this.businessName = user.getBusinessName();
                else
                        this.businessName = "NOT A BUSINESS";

        }

}
