import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PersonnelRoutingModule} from './personnel-routing.module';
import {PersonnelComponent} from './personnel.component';
import {PersonnelStudentComponent} from './personnel-student/personnel-student.component';
import {PersonnelOrganizationComponent} from './personnel-organization/personnel-organization.component';
import {NgZorroAntdModule} from "ng-zorro-antd";
import {XBreadCrumbModule} from "../../components/x-bread-crumb/x-bread-crumb.module";
import {PersonnelOrganizationCreateModalComponent} from './components/personnel-organization-create-modal/personnel-organization-create-modal.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {PersonnelContactComponent} from './personnel-contact/personnel-contact.component';
import {PersonnelContactCreateModalComponent} from './components/personnel-contact-create-modal/personnel-contact-create-modal.component';
import { PersonnelRoleTransPipe } from './pipes/personnel-role-trans.pipe';
import { PersonnelGenderTransPipe } from './pipes/personnel-gender-trans.pipe';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        PersonnelRoutingModule,
        NgZorroAntdModule,
        XBreadCrumbModule
    ],
    declarations: [
        PersonnelComponent,
        PersonnelStudentComponent,
        PersonnelOrganizationComponent,
        PersonnelOrganizationCreateModalComponent,
        PersonnelContactComponent,
        PersonnelContactCreateModalComponent,
        PersonnelRoleTransPipe,
        PersonnelGenderTransPipe
    ],
    entryComponents: [
        PersonnelOrganizationCreateModalComponent,
        PersonnelContactCreateModalComponent
    ]
})
export class PersonnelModule {
}
