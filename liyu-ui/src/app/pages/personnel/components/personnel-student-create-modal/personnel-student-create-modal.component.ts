import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Student} from '../../models/student';
import {Organization} from '../../models/organization';
import {OrganizationApiService} from '../../../../services/organization-api.service';
import {BehaviorSubject} from 'rxjs/internal/BehaviorSubject';
import {debounceTime} from 'rxjs/operators';

@Component({
    selector: 'app-personnel-student-create-modal',
    templateUrl: './personnel-student-create-modal.component.html',
    styleUrls: ['./personnel-student-create-modal.component.less'],
    providers: [OrganizationApiService]
})
export class PersonnelStudentCreateModalComponent implements OnInit {
    public studentForm: FormGroup;
    @Input('student') public student: Student;
    public organizations: Organization[];
    public page = 1;
    public size = 20;
    public total: number;
    public isLoading: boolean;
    public searchText: string;
    public searchChange$ = new BehaviorSubject('');

    constructor(private fb: FormBuilder,
                private organizationApi: OrganizationApiService) {
        this.organizations = [];
    }

    ngOnInit() {
        this.studentFormBuild();
        this.searchChange$.asObservable().pipe(debounceTime(500)).subscribe(result => {
            this.searchText = result;
            this.getOrganizationList(1, result);
        });
    }

    private studentFormBuild() {
        this.studentForm = this.fb.group({
            studentId: [this.student && this.student.studentId],
            name: [this.student && this.student.name, Validators.required],
            gender: [this.student && this.student.gender, Validators.required],
            phone: [this.student && this.student.phone, Validators.required],
            birthday: [this.student && this.student.birthday],
            organizationId: [this.student && this.student.organizationId, Validators.required]
        });
    }

    private getOrganizationList(page: number, searchText?: string) {
        this.organizationApi.miniList(page, this.size, searchText).subscribe(result => {
            this.organizations = this.organizations.concat(result.data.list);
            this.total = result.data.pagination.total;
            this.isLoading = false;
        });
    }

    public organizationSearch(value: string) {
        this.isLoading = true;
        this.organizations = [];
        this.searchChange$.next(value);
    }

    public loadMore() {
        if (this.isLoading) {
            return;
        }
        this.isLoading = true;
        this.getOrganizationList(this.page + 1, this.searchText);
    }
}
