// Toggle password visibility
document.addEventListener('DOMContentLoaded', function() {
    // Password toggle functionality
    const toggleButtons = document.querySelectorAll('.toggle-password');
    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const input = this.previousElementSibling;
            const type = input.getAttribute('type') === 'password' ? 'text' : 'password';
            input.setAttribute('type', type);
            this.innerHTML = type === 'password' ? '<i class="fas fa-eye"></i>' : '<i class="fas fa-eye-slash"></i>';
        });
    });

    // Form validation
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
            } else {
                // Show loading state
                const submitBtn = this.querySelector('button[type="submit"]');
                if (submitBtn) {
                    submitBtn.innerHTML = '<span class="loading"></span> Procesando...';
                    submitBtn.disabled = true;
                }
            }
        });
    });

    // Real-time email validation
    const emailInputs = document.querySelectorAll('input[type="email"]');
    emailInputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateEmail(this);
        });
    });

    // Password strength indicator
    const passwordInputs = document.querySelectorAll('input[type="password"]');
    passwordInputs.forEach(input => {
        if (input.id === 'password' || input.name === 'claveHash') {
            input.addEventListener('input', function() {
                checkPasswordStrength(this.value);
            });
        }
    });
});

function validateForm(form) {
    let isValid = true;
    const inputs = form.querySelectorAll('input[required], select[required]');

    inputs.forEach(input => {
        if (!input.value.trim()) {
            showFieldError(input, 'Este campo es obligatorio');
            isValid = false;
        } else {
            clearFieldError(input);
        }

        // Email validation
        if (input.type === 'email' && input.value.trim()) {
            if (!validateEmail(input)) {
                isValid = false;
            }
        }

        // Password validation
        if ((input.type === 'password' && input.value.trim()) && (input.id === 'password' || input.name === 'claveHash')) {
            if (input.value.length < 6) {
                showFieldError(input, 'La contraseña debe tener al menos 6 caracteres');
                isValid = false;
            }
        }
    });

    // Confirm password validation
    const password = form.querySelector('#password');
    const confirmPassword = form.querySelector('#confirmPassword');
    if (password && confirmPassword && password.value && confirmPassword.value) {
        if (password.value !== confirmPassword.value) {
            showFieldError(confirmPassword, 'Las contraseñas no coinciden');
            isValid = false;
        }
    }

    return isValid;
}

function validateEmail(input) {
    const email = input.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(email)) {
        showFieldError(input, 'Por favor, ingresa un email válido');
        return false;
    } else {
        clearFieldError(input);
        return true;
    }
}

function showFieldError(input, message) {
    clearFieldError(input);

    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error';
    errorDiv.style.color = '#dc3545';
    errorDiv.style.fontSize = '12px';
    errorDiv.style.marginTop = '5px';
    errorDiv.textContent = message;

    input.style.borderColor = '#dc3545';
    input.parentNode.appendChild(errorDiv);
}

function clearFieldError(input) {
    const existingError = input.parentNode.querySelector('.field-error');
    if (existingError) {
        existingError.remove();
    }
    input.style.borderColor = '#e1e5e9';
}

function checkPasswordStrength(password) {
    const strengthBar = document.getElementById('password-strength');
    if (!strengthBar) return;

    let strength = 0;
    let tips = "";

    if (password.length >= 8) strength++;
    if (password.match(/[a-z]/) && password.match(/[A-Z]/)) strength++;
    if (password.match(/\d/)) strength++;
    if (password.match(/[^a-zA-Z\d]/)) strength++;

    switch(strength) {
        case 0:
        case 1:
            strengthBar.style.width = '25%';
            strengthBar.style.background = '#dc3545';
            break;
        case 2:
            strengthBar.style.width = '50%';
            strengthBar.style.background = '#ffc107';
            break;
        case 3:
            strengthBar.style.width = '75%';
            strengthBar.style.background = '#28a745';
            break;
        case 4:
            strengthBar.style.width = '100%';
            strengthBar.style.background = '#28a745';
            break;
    }
}

// Check email availability
async function checkEmailAvailability(email) {
    if (!email) return;

    try {
        const response = await fetch(`/check-email?correo=${encodeURIComponent(email)}`);
        const isAvailable = await response.json();

        const emailInput = document.querySelector('input[type="email"]');
        if (!isAvailable) {
            showFieldError(emailInput, 'Este correo electrónico ya está registrado');
            return false;
        } else {
            clearFieldError(emailInput);
            return true;
        }
    } catch (error) {
        console.error('Error checking email:', error);
        return true;
    }
}

// Auto-hide alerts
document.addEventListener('DOMContentLoaded', function() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transition = 'opacity 0.5s ease';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });
});