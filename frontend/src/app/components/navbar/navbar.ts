import { Component, HostListener, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.html'
})
export class Navbar {
  private router = inject(Router);
  private auth = inject(AuthService);

  mobileMenuOpen = false;
  userMenuOpen = false;

  // ⭐ NEW: Scroll detection for advanced UI
  isScrolled = false;

  // Detect scroll on window
  @HostListener('window:scroll')
  onScroll() {
    this.isScrolled = window.scrollY > 10;
  }

  get isLoggedIn() {
    return this.auth.isLoggedIn();
  }

  get userRole() {
    return this.auth.getRole();
  }

  get userName() {
    return this.auth.getName();
  }

  get isFarmer() {
    return this.auth.hasRole('ROLE_FARMER');
  }

  get isAdmin() {
    return this.auth.isAdmin();
  }

  toggleMobileMenu() {
    this.mobileMenuOpen = !this.mobileMenuOpen;
    this.userMenuOpen = false;
  }

  toggleUserMenu() {
    this.userMenuOpen = !this.userMenuOpen;
  }

  closeMobileMenu() {
    this.mobileMenuOpen = false;
  }

  logout() {
    this.auth.logout();
    this.closeMobileMenu();
    this.userMenuOpen = false;
    this.router.navigate(['/login']);
  }
}
