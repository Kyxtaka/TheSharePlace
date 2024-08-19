import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule, FormBuilder, FormGroup, Validators, ReactiveFormsModule, EmailValidator } from '@angular/forms';
import { GlobalService } from '../../services/global/global.service';
import { AuthService } from '../../services/auth/auth.service';
import { TokenUtilService } from '../../services/token/token-util.service';
import { APIServiceUser } from '../../services/data/data.service';

@Component({
  selector: 'app-user-register',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    ReactiveFormsModule], 
  templateUrl: './user-register.component.html',
  styleUrl: './user-register.component.css'
})

export class UserRegisterComponent implements OnInit {
  registerForm!: FormGroup;

  constructor (
    private APIServiceUser: APIServiceUser, 
    private router: Router, 
    private globalService: GlobalService,
    private formBuilder: FormBuilder, 
    private tokenService: TokenUtilService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required] // Add confirmPassword field
    }, { 
      validator: this.passwordMatchValidator // Attach the custom validator
    });
  }

    passwordMatchValidator(formGroup: FormGroup) {
      const password = formGroup.get('password')?.value;
      const confirmPassword = formGroup.get('confirmPassword')?.value;
    
      return password === confirmPassword ? null : { mismatch: true };
    }

    onSubmit(): void {
      if (this.registerForm.valid) {
        const { firstname, lastname, email, username, password } = this.registerForm.value;
        this.APIServiceUser.register(firstname, lastname, email, username, password).subscribe({
          next: (response) => {
            console.log('Registration successful:', response);
            this.authService.login(email, password).subscribe({
              next: (authResponse) => {
                const token = authResponse['accessToken'];
                this.tokenService.setToken(token);
                this.router.navigate(['/'])
              },
              error: (error) => {
                console.log("Error while authenticating after registration provided:", error);
              }
            })
            },
            error: (error) => {
              console.error('Erreur:', error);
              if (error.status === 400) {
                // Handle BAD_REQUEST response
                const errorMessage = error.error.message || 'An error occurred';
                alert(errorMessage);
              } else {
                console.log('Unexpected error:', error);
              }
            }
          });
      }
    }

}
