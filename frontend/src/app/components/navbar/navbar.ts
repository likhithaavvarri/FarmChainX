
import { Component, HostListener, OnInit, ElementRef, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LucideAngularModule } from 'lucide-angular';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, LucideAngularModule],
  templateUrl: './navbar.html'
})
export class Navbar {
  private router = inject(Router);
  private auth = inject(AuthService);
  private host = inject(ElementRef);

  mobileMenuOpen = false;
  userMenuOpen = false;

  // ⭐ NEW: Scroll detection for advanced UI
  isScrolled = false;

  // Detect scroll on window
  @HostListener('window:scroll')
  onScroll() {
    this.isScrolled = window.scrollY > 10;
  }

  // Close user menu when clicking outside the component
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event) {
    const target = event.target as HTMLElement;
    if (!this.host.nativeElement.contains(target)) {
      this.userMenuOpen = false;
    }
  }

  ngOnInit(): void {
    // Ensure menus are closed on init/refresh
    this.userMenuOpen = false;
    this.mobileMenuOpen = false;
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