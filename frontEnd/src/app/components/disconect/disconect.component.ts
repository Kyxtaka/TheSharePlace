import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GlobalService } from '../../services/global/global.service';
import { TokenUtilService } from '../../services/token/token-util.service'; 
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-disconect',
  standalone: true,
  imports: [],
  templateUrl: './disconect.component.html',
  styleUrl: './disconect.component.css'
})
export class DisconectComponent implements OnInit{

  constructor (
    private globalService: GlobalService,
    public tokenUtilService:TokenUtilService,
    private cookiesService: CookieService,
    private authService: AuthService,
    private router: Router,
  ) {}

  ngOnInit(): void {
      console.log("loggin out")
      this.authService.logout()
      this.globalService.disconnect()
      setTimeout( () =>  {
        this.router.navigate(['/']);
      }, 2000);
      
  }
}
