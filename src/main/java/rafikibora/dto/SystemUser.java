package rafikibora.dto;

import lombok.Data;
import rafikibora.model.users.User;
@Data
public class SystemUser {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNo;
        private String createdBy;
        private String approvedBy;
        private String userid;
        private String username;
        private String mid;
        private String businessName;
        private String dateCreated;
        private String status;
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
