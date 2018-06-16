import {Component, OnInit} from '@angular/core';
import {SideNavService} from '../side-nav/side-nav.service';
import {Router} from '@angular/router';
import {ProfileService} from "../profile/profile.service";
import {TokenService} from "../../services/token.service";

@Component({
    selector: 'app-nav',
    templateUrl: './nav.component.html',
    styleUrls: ['./nav.component.less'],
    providers: []
})
export class NavComponent implements OnInit {
    public profile: any;
    public isCollapsed = false;

    constructor(private sideNavService: SideNavService,
                private token: TokenService,
                private router: Router,
                private profileService: ProfileService) {
    }

    ngOnInit() {
        this.getProfile();
    }

    public collapseSideNav() {
        this.isCollapsed = !this.isCollapsed;
        this.sideNavService.setCollapsed(this.isCollapsed);
    }

    private getProfile() {
        this.profileService.getProfile().subscribe(result => {
            this.profile = result;
        });
    }

    public profileView() {
        this.profileService.view(this.profile);
    }

    public logout() {
        this.token.clear();
        this.router.navigate(['/login']);
    }

}
