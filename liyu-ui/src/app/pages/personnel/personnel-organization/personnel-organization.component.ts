import {Component, OnInit} from '@angular/core';
import {XBreadCrumbService} from '../../../components/x-bread-crumb/x-bread-crumb.service';
import {Organization} from '../models/organization';
import {OrganizationApiService} from '../../../services/organization-api.service';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {PersonnelOrganizationCreateModalComponent} from '../components/personnel-organization-create-modal/personnel-organization-create-modal.component';
import {FormGroup} from '@angular/forms';

@Component({
    selector: 'app-personnel-organization',
    templateUrl: './personnel-organization.component.html',
    styleUrls: ['./personnel-organization.component.less'],
    providers: [OrganizationApiService]
})
export class PersonnelOrganizationComponent implements OnInit {
    public organizations: Organization[];
    public page: number;
    public total: number;
    public size: 20;

    constructor(private bcService: XBreadCrumbService,
                private msg: NzMessageService,
                private organizationApi: OrganizationApiService,
                private modalService: NzModalService) {
        this.organizations = [];
        this.page = 1;
    }

    ngOnInit() {
        this.bcService.setItems([
            {
                text: '人员管理',
                link: '/pages/personnel'
            },
            {
                text: '班级管理'
            }
        ]);
        this.getOrganizationList(this.page);
    }

    private getOrganizationList(page: number) {
        this.organizationApi.list(page, this.size).subscribe(result => {
            this.organizations = result.data.list;
            this.total = result.data.pagination.total;
            this.page = result.data.pagination.page;
        });
    }

    public organizationCreateModalOpen(organization?: Organization) {
        const modal = this.modalService.create({
            nzTitle: organization ? '编辑' : '新建' + '班级',
            nzContent: PersonnelOrganizationCreateModalComponent,
            nzComponentParams: {
                organization: organization
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
                    disabled: (a) => a.organizationForm.invalid,
                    onClick: (a) => {
                        this.organizationCreate(modal, a.organizationForm);
                    }
                }
            ]
        });

    }

    private organizationCreate(modal: NzModalRef, organizationForm: FormGroup) {
        const postData = {
            name: organizationForm.get('name').value,
            gradeId: organizationForm.get('gradeId').value,
            contactIds: organizationForm.get('contactIds').value,
        };
        const organizationId = organizationForm.get('organizationId').value;
        if (organizationId) {
            this.organizationApi.update(organizationId, postData).subscribe(result => {
                this.msg.success('更新成功');
                modal.close();
                this.getOrganizationList(this.page);
            });
        } else {
            this.organizationApi.create(postData).subscribe(result => {
                this.msg.success('创建成功');
                modal.close();
                this.getOrganizationList(this.page);
            });
        }
    }

    public organizationDelete(organization: Organization) {
        this.modalService.confirm({
            nzTitle: '确定要删除此班级吗？',
            nzOnOk: () => {
                this.organizationApi.delete(organization.organizationId).subscribe(result => {
                    this.getOrganizationList(this.page);
                    this.msg.success('删除成功');
                });
            }
        });
    }
}
