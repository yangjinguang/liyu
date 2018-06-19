import {Component, OnInit} from '@angular/core';
import {XBreadCrumbService} from '../../../components/x-bread-crumb/x-bread-crumb.service';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {StudentApiService} from '../../../services/student-api.service';
import {Student} from '../models/student';
import {PersonnelStudentCreateModalComponent} from '../components/personnel-student-create-modal/personnel-student-create-modal.component';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Organization} from '../models/organization';
import {OrganizationApiService} from '../../../services/organization-api.service';
import {BehaviorSubject} from 'rxjs/internal/BehaviorSubject';
import {debounceTime} from 'rxjs/operators';
import {StudentStatus} from '../enums/student-status';

@Component({
    selector: 'app-personnel-student',
    templateUrl: './personnel-student.component.html',
    styleUrls: ['./personnel-student.component.less'],
    providers: [StudentApiService, OrganizationApiService]
})
export class PersonnelStudentComponent implements OnInit {
    public students: Student[];
    public page: number;
    public size = 20;
    public total: number;
    public isLoading: boolean;
    public isOrganizationLoading: boolean;
    public orgPage = 1;
    public orgTotal: number;
    public orgSize = 20;
    public organizations: Organization[];
    public orgSearchText: string;
    public orgSearchChange$ = new BehaviorSubject('');
    public filterForm: FormGroup;

    constructor(private bcService: XBreadCrumbService,
                private modalService: NzModalService,
                private msg: NzMessageService,
                private studentApi: StudentApiService,
                private organizationApi: OrganizationApiService,
                private fb: FormBuilder) {
        this.page = 1;
        this.students = [];
        this.organizations = [];
    }

    ngOnInit() {
        this.bcService.setItems([
            {
                text: '人员管理',
                link: '/pages/personnel'
            },
            {
                text: '幼儿管理'
            }
        ]);
        this.filterFormBuild();
        this.getStudentList(this.page);
        this.orgSearchChange$.asObservable().pipe(debounceTime(500)).subscribe(result => {
            this.orgSearchText = result;
            this.getOrganizationList(1);
        });
    }

    private getStudentList(page: number, searchData?: object) {
        searchData = searchData || {};
        searchData['page'] = page;
        searchData['size'] = this.size;
        this.isLoading = true;
        this.studentApi.list(searchData).subscribe(result => {
            this.students = result.data.list;
            this.page = result.data.pagination.page;
            this.total = result.data.pagination.total;
            this.isLoading = false;
        });
    }

    private filterFormBuild() {
        this.filterForm = this.fb.group({
            name: [],
            phone: [],
            organizationId: []
        });
    }

    public studentCreateModalOpen(student?: Student) {
        const modal = this.modalService.create({
            nzTitle: student ? '编辑' : '添加' + '幼儿',
            nzContent: PersonnelStudentCreateModalComponent,
            nzComponentParams: {
                student: student
            },
            nzFooter: [
                {
                    label: '取消',
                    type: 'default',
                    onClick: () => {
                        modal.close();
                    }
                },
                {
                    label: '提交',
                    type: 'primary',
                    disabled: (a) => a.studentForm.invalid,
                    onClick: (a) => {
                        this.studentCreate(modal, a.studentForm);
                    }
                }
            ]
        });
    }

    private studentCreate(modal: NzModalRef, studentForm: FormGroup) {
        const postData = {
            name: studentForm.get('name').value,
            gender: studentForm.get('gender').value,
            phone: studentForm.get('phone').value,
            birthday: studentForm.get('birthday').value,
            organizationId: studentForm.get('organizationId').value,
        };
        const studentId = studentForm.get('studentId').value;
        if (studentId) {
            this.studentApi.update(studentId, postData).subscribe(result => {
                modal.close();
                this.getStudentList(this.page);
                this.msg.success('更新成功');
            });
        } else {
            this.studentApi.create(postData).subscribe(result => {
                modal.close();
                this.getStudentList(this.page);
                this.msg.success('创建成功');
            });
        }
    }

    public studentDelete(student: Student) {
        this.modalService.confirm({
            nzTitle: '确定要删除此幼儿吗？',
            nzOnOk: () => {
                this.studentApi.changeStatus(student.studentId, StudentStatus.DELETED).subscribe(result => {
                    this.getStudentList(this.page);
                    this.msg.success('删除成功');
                });
            }
        });
    }

    private getOrganizationList(orgPage: number, searchData?: object) {
        searchData = searchData || {};
        searchData['page'] = orgPage;
        searchData['size'] = this.orgSize;
        searchData['orgSearchText'] = this.orgSearchText;
        this.organizationApi.miniList(searchData).subscribe(result => {
            this.organizations = this.organizations.concat(result.data.list);
            this.orgPage = result.data.pagination.page;
            this.orgTotal = result.data.pagination.total;
            this.isOrganizationLoading = false;
        });
    }

    public organizationSearch(orgSearchText: string) {
        this.isOrganizationLoading = true;
        this.organizations = [];
        this.orgSearchChange$.next(orgSearchText);
    }

    public organizationLoadMore() {
        if (this.isOrganizationLoading) {
            return;
        }
        this.isOrganizationLoading = true;
        this.getOrganizationList(this.orgPage + 1);
    }

    public toSearch() {
        const searchData = {
            name: this.filterForm.get('name').value,
            phone: this.filterForm.get('phone').value,
            organizationId: this.filterForm.get('organizationId').value
        };
        this.getStudentList(this.page, searchData);
    }
}
