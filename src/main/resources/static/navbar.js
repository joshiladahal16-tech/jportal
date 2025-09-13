// Unified Navbar Management for Job Portal

// Function to update navbar based on login status
function updateNavbar() {
  const isLoggedIn = localStorage.getItem("isLoggedIn") === "true";
  const userRole = localStorage.getItem("userRole");
  const navLinks = document.querySelector(".nav-links");

  if (!navLinks) return;

  // Clear existing nav links
  navLinks.innerHTML = "";

  if (isLoggedIn) {
    // User is logged in - show appropriate nav based on role
    if (userRole === "Employer") {
      // Employer navigation
      navLinks.innerHTML = `
        <a href="employer-dashboard.html">Dashboard</a>
        <a href="employer-post-job.html">Post Job</a>
        <a href="employer-manage-jobs.html">Manage Jobs</a>
        <a href="employer-applicants.html">Applicants</a>
        <a href="#" onclick="logout()" class="btn btn-outline">Log Out</a>
      `;
    } else if (userRole === "Admin") {
      // Admin navigation
      navLinks.innerHTML = `
        <a href="admin-dashboard.html">Dashboard</a>
        <a href="admin-users.html">Users</a>
        <a href="admin-jobs.html">Jobs</a>
        <a href="admin-employers.html">Employers</a>
        <a href="#" onclick="logout()" class="btn btn-outline">Log Out</a>
      `;
    } else {
      // Job Seeker navigation (default)
      navLinks.innerHTML = `
        <a href="user-jobs.html">Browse Jobs</a>
        <a href="user-dashboard.html">Dashboard</a>
        <a href="user-applied.html">Applied</a>
        <a href="user-profile.html">Profile</a>
        <a href="#" onclick="logout()" class="btn btn-outline">Log Out</a>
      `;
    }
  } else {
    // User is not logged in - show public navigation
    navLinks.innerHTML = `
      <a href="user-jobs.html">Browse Jobs</a>
      <a href="user-login.html" class="btn btn-outline">Login</a>
      <a href="user-register.html" class="btn btn-primary">Sign Up</a>
    `;
  }
}

// Logout function
function logout() {
  localStorage.removeItem("userEmail");
  localStorage.removeItem("userRole");
  localStorage.removeItem("isLoggedIn");
  window.location.href = "index.html";
}

// Update navbar when page loads
document.addEventListener("DOMContentLoaded", updateNavbar);

// Also update navbar after a short delay to ensure all elements are loaded
setTimeout(updateNavbar, 100);


