import {Component, OnInit} from '@angular/core';
import {Contact} from '../../../models/contact';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {PersonnelContactCreateModalComponent} from '../components/personnel-contact-create-modal/personnel-contact-create-modal.component';
import {FormGroup} from '@angular/forms';
import {ContactApiService} from '../../../services/contact-api.service';
import {RoleApiService} from '../../../services/role-api.service';
import {Role} from '../../../models/role';
import {ContactStatus} from '../enums/contact-status';
import {XBreadCrumbService} from '../../../components/x-bread-crumb/x-bread-crumb.service';

@Component({
    selector: 'app-personnel-contact',
    templateUrl: './personnel-contact.component.html',
    styleUrls: ['./personnel-contact.component.less'],
    providers: [RoleApiService]
})
export class PersonnelContactComponent implements OnInit {
    public contacts: Contact[];
    public page: number;
    public size = 20;
    public total: number;
    public roles: Role[];
    public isLoading: boolean;

    constructor(private modalService: NzModalService,
                private contactApi: ContactApiService,
                private roleApi: RoleApiService,
                private msg: NzMessageService,
                private bcService: XBreadCrumbService) {
        this.page = 1;
    }

    ngOnInit() {
        this.bcService.setItems([
            {
                text: '人员管理',
                link: '/pages/personnel'
            },
            {
                text: '员工管理'
            }
        ]);
        this.getRoleList();
        this.getContactList(this.page);
    }

    private getContactList(page: number) {
        this.isLoading = true;
        this.contactApi.list(page, this.size).subscribe(result => {
            this.contacts = result.data.list;
            this.page = result.data.pagination.page;
            this.total = result.data.pagination.total;
            this.isLoading = false;
        });
    }

    private getRoleList() {
        this.roleApi.list().subscribe(result => {
            this.roles = result.data;
        });
    }

    public contactCreateModalOpen(contact?: Contact) {
        const modal = this.modalService.create({
            nzTitle: contact ? '编辑' : '创建' + '员工',
            nzContent: PersonnelContactCreateModalComponent,
            nzComponentParams: {
                contact: contact
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
                    disabled: (a) => a.contactForm.invalid,
                    onClick: (a) => {
                        this.contactCreate(modal, a.contactForm);
                    }
                }
            ]
        });
    }

    private contactCreate(modal: NzModalRef, contactForm: FormGroup) {
        const postData = {
            name: contactForm.get('name').value,
            gender: contactForm.get('gender').value,
            phone: contactForm.get('phone').value,
            email: contactForm.get('email').value,
            roleId: contactForm.get('roleId').value
        };
        const contactId = contactForm.get('contactId').value;
        if (contactId) {
            this.contactApi.update(contactId, postData).subscribe(result => {
                modal.close();
                this.getContactList(this.page);
                this.msg.success('修改成功');
            });
        } else {
            this.contactApi.create(postData).subscribe(result => {
                modal.close();
                this.getContactList(this.page);
                this.msg.success('创建成功');
            });
        }
    }

    public contactDelete(contact: Contact) {
        this.modalService.confirm({
            nzTitle: '确定要删除此员工吗?',
            nzOnOk: () => {
                this.contactApi.changeStatus(contact.contactId, ContactStatus.DELETED).subscribe(result => {
                    this.getContactList(this.page);
                    this.msg.success('删除成功');
                });
            }
        });
    }
}
