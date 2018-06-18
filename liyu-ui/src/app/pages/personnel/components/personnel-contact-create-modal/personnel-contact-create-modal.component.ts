import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Role} from "../../../../models/role";
import {Contact} from "../../../../models/contact";
import {RoleApiService} from "../../../../services/role-api.service";

@Component({
    selector: 'app-personnel-contact-create-modal',
    templateUrl: './personnel-contact-create-modal.component.html',
    styleUrls: ['./personnel-contact-create-modal.component.less'],
    providers: [RoleApiService]
})
export class PersonnelContactCreateModalComponent implements OnInit {
    public contactForm: FormGroup;
    public roles: Role[];
    @Input('contact') public contact: Contact;

    constructor(private fb: FormBuilder,
                private roleApi: RoleApiService) {
    }

    ngOnInit() {
        this.getRoleList();
        this.contactFormBuild();
    }

    private getRoleList() {
        this.roleApi.list().subscribe(result => {
            this.roles = result.data;
        })
    }

    private contactFormBuild() {
        this.contactForm = this.fb.group({
            contactId: [this.contact && this.contact.contactId],
            name: [this.contact && this.contact.name, Validators.required],
            gender: [this.contact && this.contact.gender, Validators.required],
            phone: [this.contact && this.contact.phone, Validators.required],
            email: [this.contact && this.contact.email],
            roleId: [this.contact && this.contact.roleId, Validators.required],
        })
    }

}
