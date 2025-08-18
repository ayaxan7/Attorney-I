# Attorney-I

## Overview

AttorneyI is a cross-platform legal news and updates application built with Kotlin Multiplatform and Compose Multiplatform. The application provides legal professionals and enthusiasts with real-time access to curated legal news, case updates, regulatory changes, and industry insights from trusted legal sources.

## Product Philosophy

AttorneyI is designed as a comprehensive legal information platform that bridges the gap between complex legal developments and accessible, actionable insights. The application serves as a centralized hub for legal professionals to stay informed about:

- **Breaking Legal News**: Real-time updates on significant legal developments
- **Case Law Updates**: Latest court decisions and their implications
- **Regulatory Changes**: New laws, amendments, and policy updates
- **Industry Analysis**: Expert commentary and legal trend analysis
- **Practice Area Insights**: Specialized content across different legal domains

## Technical Architecture

### Core Technology Stack

**Framework & Platform**
- **Kotlin Multiplatform**: Shared business logic across Android and iOS platforms
- **Compose Multiplatform**: Modern declarative UI framework for both platforms
- **Target Platforms**: Android (API 28+) and iOS (iOS 15+)

**Networking & Data**
- **Ktor Client**: Type-safe HTTP client for API communication
- **Kotlinx Serialization**: JSON serialization and deserialization
- **Custom Legal News API**: Backend service hosted at `attorney-i.onrender.com`

**Architecture Pattern**
- **MVVM (Model-View-ViewModel)**: Clean separation of concerns
- **Repository Pattern**: Centralized data access layer
- **Dependency Injection**: Koin framework for modular dependency management

**UI & User Experience**
- **Material Design 3**: Modern, accessible design system
- **Coil**: Efficient image loading and caching
- **SwipeRefresh**: Pull-to-refresh functionality
- **Responsive Design**: Adaptive layouts for different screen sizes

### Key Features

**Content Discovery**
- Advanced search functionality with real-time filtering
- Tag-based categorization system for legal topics
- Breaking news notifications and highlights
- Source-based content filtering

**User Experience**
- Intuitive navigation and content consumption
- Offline reading capabilities
- Share functionality for articles and insights
- Dark/Light theme support

**Performance Optimizations**
- Efficient image loading and caching
- Network request optimization with timeouts
- Lazy loading for large content lists
- State management for seamless user experience

## Development Setup

### Prerequisites

- **Android Studio**: Arctic Fox or later with Kotlin Multiplatform plugin
- **Xcode**: 14.0+ (for iOS development)
- **JDK**: Version 11 or higher
- **Kotlin**: Version 2.2.0
- **Gradle**: Version 8.12.0

### Local Configuration

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd AttorneyI
   ```

2. **Configure API Keys**
   Create a `local.properties` file in the root directory:
   ```properties
   # Android SDK path (auto-generated)
   sdk.dir=/path/to/your/android/sdk
   ```

3. **Install Dependencies**
   ```bash
   ./gradlew build
   ```

4. **Platform-Specific Setup**

   **Android**
   ```bash
   # Open project in Android Studio
   # Sync Gradle files
   ./gradlew :composeApp:assembleDebug
   ```

   **iOS**
   ```bash
   # Generate Xcode project
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   # Open iosApp/iosApp.xcodeproj in Xcode
   ```

### iOS Local Configuration

#### Prerequisites for iOS Development

1. **macOS Requirements**
   - macOS Monterey (12.0) or later
   - At least 8GB RAM (16GB recommended)
   - 50GB+ free disk space

2. **Xcode Installation**
   ```bash
   # Install Xcode from App Store or Apple Developer Portal
   # Verify installation
   xcode-select --install
   ```

3. **Command Line Tools**
   ```bash
   # Install Xcode Command Line Tools
   sudo xcode-select --install
   
   # Verify installation
   xcode-select -p
   ```

#### iOS Project Setup

1. **Initial Project Configuration**
   ```bash
   # Navigate to project root
   cd AttorneyI
   
   # Clean any existing builds
   ./gradlew clean
   
   # Build shared framework for iOS
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```

2. **Xcode Project Setup**
   ```bash
   # Open the iOS project in Xcode
   open iosApp/iosApp.xcodeproj
   ```

3. **Bundle Identifier Configuration**
   - In Xcode, select the project navigator
   - Click on "iosApp" project
   - Under "Targets", select "iosApp"
   - In "General" tab, update the Bundle Identifier:
     ```
     com.yourcompany.attorneyi
     ```

4. **Team and Signing Configuration**
   - In the "Signing & Capabilities" tab
   - Select your Apple Developer Team
   - Ensure "Automatically manage signing" is checked
   - Verify the provisioning profile is valid

#### iOS Simulator Configuration

1. **Install iOS Simulators**
   ```bash
   # List available simulators
   xcrun simctl list devices
   
   # Install specific iOS version (if needed)
   # This is done through Xcode -> Preferences -> Components
   ```

2. **Run on Simulator**
   ```bash
   # Build and run on simulator from command line
   ./gradlew :composeApp:iosSimulatorArm64Test
   
   # Or use Xcode: Product -> Run (⌘+R)
   ```

#### iOS Device Configuration

1. **Physical Device Setup**
   - Connect iOS device via USB
   - Trust the computer on the device
   - Enable Developer Mode (iOS 16+):
     - Settings → Privacy & Security → Developer Mode → Enable

2. **Device Registration**
   - In Xcode, go to Window → Devices and Simulators
   - Select your device and click "Use for Development"
   - Enter Apple ID credentials if prompted

3. **Running on Device**
   ```bash
   # Build for device
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   
   # Deploy via Xcode: Select device and run
   ```

#### iOS-Specific Configuration Files

1. **Info.plist Configuration**
   Located at `iosApp/iosApp/Info.plist`:
   ```xml
   <key>NSAppTransportSecurity</key>
   <dict>
       <key>NSAllowsArbitraryLoads</key>
       <true/>
   </dict>
   <key>CFBundleDisplayName</key>
   <string>AttorneyI</string>
   <key>CFBundleVersion</key>
   <string>1.0</string>
   ```

2. **Build Configuration**
   Located at `iosApp/Configuration/Config.xcconfig`:
   ```
   DEVELOPMENT_TEAM = YOUR_TEAM_ID
   BUNDLE_ID = com.yourcompany.attorneyi
   APP_NAME = AttorneyI
   ```

#### Troubleshooting iOS Setup

1. **Common Build Issues**
   ```bash
   # Clean derived data
   rm -rf ~/Library/Developer/Xcode/DerivedData
   
   # Clean and rebuild
   ./gradlew clean
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```

2. **Framework Issues**
   ```bash
   # Verify framework generation
   ls -la composeApp/build/xcode-frameworks/
   
   # Regenerate if missing
   ./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
   ```

3. **Signing Issues**
   - Verify Apple Developer account status
   - Check Bundle Identifier uniqueness
   - Ensure provisioning profile is valid
   - Try manual signing if automatic fails

4. **Simulator Issues**
   ```bash
   # Reset simulator
   xcrun simctl erase all
   
   # Restart simulator service
   sudo killall -9 com.apple.CoreSimulator.CoreSimulatorService
   ```

### Environment Variables

The application requires the following configuration in `local.properties`:

| Variable | Purpose | Required | Example |
|----------|---------|----------|---------|
| `sdk.dir` | Android SDK path | Yes (Android only) | `/Users/username/Library/Android/sdk` |

### Build Variants

- **Debug**: Development build with logging enabled
- **Release**: Production-ready build with optimizations

## Project Architecture

### Module Organization

```
composeApp/
├── src/
│   ├── commonMain/          # Shared code across platforms
│   │   ├── kotlin/
│   │   │   ├── data/        # Data layer (repositories, models, APIs)
│   │   │   │   ├── config/  # API configuration
│   │   │   │   ├── model/   # Data models and DTOs
│   │   │   │   ├── remote/  # Network API services
│   │   │   │   └── repository/ # Repository implementations
│   │   │   ├── di/          # Dependency injection modules
│   │   │   ├── presentation/ # UI layer (screens, ViewModels)
│   │   │   │   ├── legalUpdates/ # News feature module
│   │   │   │   │   ├── components/ # Reusable UI components
│   │   │   │   │   └── state/ # UI state management
│   │   │   │   └── ui/      # Theme and design system
│   │   │   └── utils/       # Shared utilities
│   ├── androidMain/         # Android-specific implementations
│   │   └── kotlin/
│   │       ├── utils/       # Android platform utilities
│   │       └── data/config/ # Android platform config
│   └── iosMain/            # iOS-specific implementations
│       └── kotlin/
│           ├── utils/       # iOS platform utilities
│           └── data/config/ # iOS platform config
```

### Component Descriptions

**NewsScreen**: Main interface displaying legal news and updates
**NewsViewModel**: Manages news data, search, and filtering logic
**NewsRepository**: Handles data fetching and caching
**NewsApiService**: Network interface for legal news API

## API Integration

### Legal News API

The application integrates with a custom legal news API that provides:

- **Endpoint**: `https://attorney-i.onrender.com/api`
- **Authentication**: API key-based authentication
- **Rate Limiting**: Respectful API usage with built-in timeouts
- **Response Format**: JSON with standardized legal article structure

### Data Models

- **LegalArticle**: Core news article with legal metadata
- **LegalSource**: Trusted legal publication information
- **LegalNewsResponse**: API response wrapper with status and data

## Testing Strategy

### Test Coverage
- **Unit Tests**: Business logic and data layer testing
- **UI Tests**: User interaction and navigation testing
- **Integration Tests**: API and data flow validation

### Quality Assurance
- **Code Linting**: Kotlin coding standards enforcement
- **Static Analysis**: Code quality and security scanning
- **Performance Monitoring**: Memory and network usage optimization

## Contribution Guidelines

### Development Workflow

1. **Fork and Clone**: Create your development environment
2. **Feature Branch**: Create feature-specific branches from `main`
3. **Development**: Follow established coding standards and patterns
4. **Testing**: Ensure all tests pass and add new tests for features
5. **Documentation**: Update relevant documentation
6. **Pull Request**: Submit for code review with detailed description

### Code Standards

- **Kotlin Coding Conventions**: Follow official Kotlin style guide
- **Compose Best Practices**: Implement efficient, reusable UI components
- **Clean Architecture**: Maintain clear separation between layers
- **Documentation**: Comprehensive KDoc for public APIs

### Commit Guidelines

```
feat: add new legal category filtering
fix: resolve search performance issue
docs: update API integration guide
test: add unit tests for news repository
refactor: optimize article loading logic
```

### Review Process

1. **Automated Checks**: CI/CD pipeline validation
2. **Code Review**: Peer review for code quality and standards
3. **QA Testing**: Functional and regression testing
