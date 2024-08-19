import { Component, ViewEncapsulation, OnInit } from '@angular/core';
import { RouterModule, RouterOutlet, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { GlobalService } from '../../services/global/global.service';
import { TokenUtilService } from '../../services/token/token-util.service'; 
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../../services/auth/auth.service';


@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [
    RouterModule,
    CommonModule,
    RouterOutlet
  ],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.css'
})
export class WelcomeComponent implements OnInit {
  public isLogged: boolean = false;
  public username: string | undefined;

  constructor (
    private globalService: GlobalService,
    public tokenUtilService:TokenUtilService,
    private cookiesService: CookieService,
    private authService: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const token = this.cookiesService.get('authToken');
    console.log("CSRF token from cookie", "Desactivated");

    // Token is valid, skip login and navigate to home or dashboard
    if (token && !this.tokenUtilService.isTokenExpired(token)) {
      this.username = this.tokenUtilService.extractTokenUsername(token);
      this.globalService.init(this.username); // Init user Data from username
    } else {
      // Token is not present or expired, navigate to login
      this.globalService.updateIsLogged(false);
      this.authService.logout()
    }

    this.globalService.isLogged$.subscribe({
      next: () => {
        this.isLogged = this.globalService.getCurrentIsLogged();
        console.log("isLogged status:", this.isLogged);
      }
    })

    this.globalService.userData$.subscribe({
      next: () => {
        this.username = this.globalService.getCurrentUserData()?.username
      }
    })
  }
}
