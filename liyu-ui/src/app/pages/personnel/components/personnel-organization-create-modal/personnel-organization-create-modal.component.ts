import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Organization} from '../../models/organization';
import {GradeApiService} from '../../../../services/grade-api.service';
import {Grade} from '../../models/grade';
import {Contact} from '../../../../models/contact';
import {ContactApiService} from '../../../../services/contact-api.service';
import {BehaviorSubject} from 'rxjs/internal/BehaviorSubject';
import {debounceTime} from 'rxjs/operators';

@Component({
    selector: 'app-personnel-organization-create-modal',
    templateUrl: './personnel-organization-create-modal.component.html',
    styleUrls: ['./personnel-organization-create-modal.component.less'],
    providers: [GradeApiService, ContactApiService]
})
export class PersonnelOrganizationCreateModalComponent implements OnInit {
    public organizationForm: FormGroup;
    public grades: Grade[];
    @Input('organization') public organization: Organization;
    public contacts: Contact[];
    public page: number;
    public size = 20;
    public total: number;
    public searchChange$ = new BehaviorSubject('');
    public isLoading: boolean;
    private searchText: string;

    constructor(private fb: FormBuilder,
                private contactApi: ContactApiService,
                private gradeApi: GradeApiService) {
        this.page = 1;
        this.contacts = [];

    }

    ngOnInit() {
        this.getGradeList();
        this.searchChange$.asObservable().pipe(debounceTime(500)).subscribe(result => {
            this.searchText = result;
            this.contacts = [];
            this.getContactList(1, result);
        });
        // this.getContactList(this.page);
        this.organizationFormBuild();


    }

    private getGradeList() {
        this.gradeApi.list().subscribe(result => {
            this.grades = result.data;
        });
    }

    private getContactList(page: number, searchText?: string) {
        this.contactApi.list(page, this.size, searchText).subscribe(result => {
            this.contacts = this.contacts.concat(result.data.list);
            this.page = result.data.pagination.page;
            this.total = result.data.pagination.total;
            this.isLoading = false;
        });
    }

    private organizationFormBuild() {
        this.organizationForm = this.fb.group({
            organizationId: [this.organization && this.organization.organizationId],
            name: [this.organization && this.organization.name, Validators.required],
            gradeId: [this.organization && this.organization.gradeId, Validators.required],
            contactIds: [this.organization && this.organization.contactIds || []]
        });
    }

    public searchContact(value: string) {
        this.isLoading = true;
        this.searchChange$.next(value);
    }

    public loadMore() {
        if (this.isLoading) {
            return;
        }
        this.isLoading = true;
        this.getContactList(this.page + 1, this.searchText);
    }
}
