
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

type PrimaryRole = 'ADMIN' | 'FARMER' | 'DISTRIBUTOR' | 'RETAILER' | 'CONSUMER' | 'USER';

interface User {
  id: string;
  name: string;
  email: string;
  roles: string[];
  isAdmin: boolean;
  primaryRole: PrimaryRole;
}

@Component({
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-users.html',
  styleUrls: ['./admin-users.scss']
})
export class AdminUsers implements OnInit {
  users: User[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  private normalizeRole(r: any): string {
    const val = (r?.name ?? r ?? '').toString().toUpperCase();
    return val.startsWith('ROLE_') ? val : `ROLE_${val}`;
  }

  private derivePrimaryRole(roles: string[]): PrimaryRole {
    const set = new Set(roles.map(this.normalizeRole));
    if (set.has('ROLE_ADMIN')) return 'ADMIN';
    if (set.has('ROLE_FARMER')) return 'FARMER';
    if (set.has('ROLE_DISTRIBUTOR')) return 'DISTRIBUTOR';
    if (set.has('ROLE_RETAILER')) return 'RETAILER';
    if (set.has('ROLE_CONSUMER')) return 'CONSUMER';
    return 'USER';
    }

  loadUsers(): void {
    this.http.get<any[]>('/api/admin/users').subscribe(data => {
      this.users = data.map(u => {
        const roles = (u.roles ?? []).map((r: any) => this.normalizeRole(r));
        const isAdmin = roles.includes('ROLE_ADMIN');
        return {
          id: u.id,
          name: u.name,
          email: u.email,
          roles,
          isAdmin,
          primaryRole: this.derivePrimaryRole(roles)
        };
      });
    });
  }

  roleClass(role: PrimaryRole): string {
    switch (role) {
      case 'ADMIN': return 'bg-purple-100 text-purple-700';
      case 'FARMER': return 'bg-emerald-100 text-emerald-700';
      case 'DISTRIBUTOR': return 'bg-cyan-100 text-cyan-700';
      case 'RETAILER': return 'bg-amber-100 text-amber-700';
      case 'CONSUMER': return 'bg-slate-100 text-slate-700';
      default: return 'bg-gray-100 text-gray-700';
    }
  }

  promote(userId: string): void {
    if (!confirm('Promote this user to Admin?')) return;
    this.http.post(`/api/admin/promote/${userId}`, {}).subscribe({
      next: () => {
        alert('User promoted to Admin successfully!');
        this.loadUsers();
      },
      error: (err) => alert(err.error?.message || 'Promotion failed')
    });
  }
}