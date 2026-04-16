// NutriTrack Web Application JavaScript
// Common utilities and functions

// Global configuration
const NutriTrack = {
    apiBaseUrl: '/api',
    version: '1.0.0'
};

// Utility functions
NutriTrack.utils = {
    // Format number with specified decimal places
    formatNumber: (num, decimals = 1) => {
        return Math.round(num * Math.pow(10, decimals)) / Math.pow(10, decimals);
    },
    
    // Format calories with appropriate unit
    formatCalories: (calories) => {
        return Math.round(calories) + ' kcal';
    },
    
    // Format weight with appropriate unit
    formatWeight: (weight) => {
        return Math.round(weight * 10) / 10 + 'g';
    },
    
    // Get BMI category color
    getBMICategoryColor: (category) => {
        switch (category.toLowerCase()) {
            case 'underweight': return '#2196F3';
            case 'normal weight': return '#4CAF50';
            case 'overweight': return '#FF9800';
            case 'obese': return '#F44336';
            default: return '#9E9E9E';
        }
    },
    
    // Show loading state
    showLoading: (elementId) => {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = `
                <div class="loading">
                    <div class="spinner-border" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <p class="mt-2">Loading...</p>
                </div>
            `;
        }
    },
    
    // Show error message
    showError: (elementId, message) => {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = `
                <div class="alert alert-danger">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    ${message}
                </div>
            `;
        }
    },
    
    // Show success message
    showSuccess: (elementId, message) => {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = `
                <div class="alert alert-success">
                    <i class="fas fa-check-circle me-2"></i>
                    ${message}
                </div>
            `;
        }
    }
};

// API utilities
NutriTrack.api = {
    // Make API request with error handling
    request: async (endpoint, options = {}) => {
        try {
            const response = await fetch(NutriTrack.apiBaseUrl + endpoint, {
                headers: {
                    'Content-Type': 'application/json',
                    ...options.headers
                },
                ...options
            });
            
            const data = await response.json();
            
            if (!response.ok) {
                throw new Error(data.message || 'Request failed');
            }
            
            return data;
        } catch (error) {
            console.error('API request error:', error);
            throw error;
        }
    },
    
    // Get request
    get: (endpoint) => {
        return NutriTrack.api.request(endpoint);
    },
    
    // Post request
    post: (endpoint, data) => {
        return NutriTrack.api.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    // Put request
    put: (endpoint, data) => {
        return NutriTrack.api.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    
    // Delete request
    delete: (endpoint) => {
        return NutriTrack.api.request(endpoint, {
            method: 'DELETE'
        });
    }
};

// Common UI functions
NutriTrack.ui = {
    // Show alert message
    showAlert: (message, type = 'info', containerId = 'alertContainer') => {
        const container = document.getElementById(containerId);
        if (!container) return;
        
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        container.appendChild(alertDiv);
        
        // Auto-remove after 5 seconds
        setTimeout(() => {
            alertDiv.remove();
        }, 5000);
    },
    
    // Show loading spinner
    showLoading: (elementId, message = 'Loading...') => {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = `
                <div class="loading">
                    <div class="spinner-border" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div>
                    <p class="mt-2">${message}</p>
                </div>
            `;
        }
    },
    
    // Hide loading spinner
    hideLoading: (elementId) => {
        const element = document.getElementById(elementId);
        if (element) {
            const loadingElement = element.querySelector('.loading');
            if (loadingElement) {
                loadingElement.remove();
            }
        }
    },
    
    // Format date for display
    formatDate: (date) => {
        if (typeof date === 'string') {
            date = new Date(date);
        }
        return date.toLocaleDateString('en-US', {
            weekday: 'short',
            year: 'numeric',
            month: 'short',
            day: 'numeric'
        });
    },
    
    // Animate number counting
    animateNumber: (elementId, targetValue, duration = 1000) => {
        const element = document.getElementById(elementId);
        if (!element) return;
        
        const startValue = 0;
        const increment = targetValue / (duration / 16);
        let currentValue = startValue;
        
        const timer = setInterval(() => {
            currentValue += increment;
            
            if (currentValue >= targetValue) {
                currentValue = targetValue;
                clearInterval(timer);
            }
            
            element.textContent = Math.round(currentValue);
        }, 16);
    }
};

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    console.log('NutriTrack Web Application v' + NutriTrack.version + ' loaded');
    
    // Add any global initialization here
    NutriTrack.ui.hideLoading('loading');
});

// Export for use in other files
window.NutriTrack = NutriTrack;
