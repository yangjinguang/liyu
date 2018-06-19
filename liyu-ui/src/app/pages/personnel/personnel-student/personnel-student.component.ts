import {Component, OnInit} from '@angular/core';
import {XBreadCrumbService} from '../../../components/x-bread-crumb/x-bread-crumb.service';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {StudentApiService} from '../../../services/student-api.service';
import {Student} from '../models/student';
import {PersonnelStudentCreateModalComponent} from '../components/personnel-student-create-modal/personnel-student-create-modal.component';
import {FormGroup} from '@angular/forms';

@Component({
    selector: 'app-personnel-student',
    templateUrl: './personnel-student.component.html',
    styleUrls: ['./personnel-student.component.less'],
    providers: [StudentApiService]
})
export class PersonnelStudentComponent implements OnInit {
    public students: Student[];
    public page: number;
    public size = 20;
    public total: number;

    constructor(private bcService: XBreadCrumbService,
                private modalService: NzModalService,
                private msg: NzMessageService,
                private studentApi: StudentApiService) {
        this.page = 1;
        this.students = [];
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
        this.getStudentList(this.page);
    }

    private getStudentList(page: number) {
        this.studentApi.list(page, this.size).subscribe(result => {
            this.students = result.data.list;
            this.page = result.data.pagination.page;
            this.total = result.data.pagination.total;
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

    }
}
