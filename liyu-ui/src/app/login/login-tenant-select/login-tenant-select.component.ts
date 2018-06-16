import {Component, Input, OnInit} from '@angular/core';
import {NzModalRef} from 'ng-zorro-antd';
import {Tenant} from "../../models/tenant";

@Component({
    selector: 'app-login-tenant-select',
    templateUrl: './login-tenant-select.component.html',
    styleUrls: ['./login-tenant-select.component.less']
})
export class LoginTenantSelectComponent implements OnInit {
    @Input() public tenants: Tenant[];

    constructor(private modalRef: NzModalRef) {
    }

    ngOnInit() {
    }

    public selected(tenant: Tenant) {
        this.modalRef.close(tenant);
    }
}
