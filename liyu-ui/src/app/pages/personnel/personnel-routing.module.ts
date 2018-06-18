import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {PersonnelStudentComponent} from "./personnel-student/personnel-student.component";
import {PersonnelOrganizationComponent} from "./personnel-organization/personnel-organization.component";
import {PersonnelComponent} from "./personnel.component";
import {PersonnelContactComponent} from "./personnel-contact/personnel-contact.component";

const routes: Routes = [
    {
        path: '',
        component: PersonnelComponent,
        children: [
            {
                path: 'organization',
                component: PersonnelOrganizationComponent
            },
            {
                path: 'student',
                component: PersonnelStudentComponent
            },
            {
                path: 'contact',
                component: PersonnelContactComponent
            },
            {
                path: '',
                redirectTo: 'organization',
                pathMatch: 'full'
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class PersonnelRoutingModule {
}
