
$ErrorActionPreference = "Stop"

function Test-Endpoint {
    param (
        [string]$Url,
        [string]$Method = "GET",
        [hashtable]$Headers = @{},
        [string]$Body = $null,
        [string]$Description
    )

    Write-Host "Testing $Description..." -NoNewline
    try {
        $params = @{
            Uri = $Url
            Method = $Method
            ContentType = "application/json"
        }
        if ($Headers.Keys.Count -gt 0) { $params.Headers = $Headers }
        if ($Body) { $params.Body = $Body }

        $response = Invoke-RestMethod @params
        Write-Host " [OK]" -ForegroundColor Green
        return $response
    } catch {
        Write-Host " [FAILED]" -ForegroundColor Red
        Write-Host $_.Exception.Message
        if ($_.Exception.Response) {
             # Read the response stream if available
             $stream = $_.Exception.Response.GetResponseStream()
             if ($stream) {
                 $reader = New-Object System.IO.StreamReader($stream)
                 Write-Host "Response Body: $($reader.ReadToEnd())"
             }
        }
        return $null
    }
}

# 1. Register
$email = "test_$(Get-Random)@example.com"
$registerBody = @{
    name = "Test User"
    email = $email
    password = "password123"
    phone = "555-0199"
} | ConvertTo-Json

$registerResponse = Test-Endpoint -Url "http://localhost:8081/api/auth/register" -Method "POST" -Body $registerBody -Description "Registration"

# 2. Login
$loginBody = @{
    email = $email
    password = "password123"
} | ConvertTo-Json

$token = Test-Endpoint -Url "http://localhost:8081/api/auth/login" -Method "POST" -Body $loginBody -Description "Login"

if (-not $token) {
    Write-Error "Login failed, cannot proceed."
}

Write-Host "Token received: $token"

# 3. Get Doctors
$headers = @{
    Authorization = "Bearer $token"
}

$doctors = Test-Endpoint -Url "http://localhost:8081/api/patients/doctors" -Headers $headers -Description "Fetch Doctors"
Write-Host "Doctors Found: $($doctors.Count)"
$doctors | Format-Table

# 4. Get Profile
$profile = Test-Endpoint -Url "http://localhost:8081/api/patients/profile" -Headers $headers -Description "Fetch Profile"
Write-Host "Profile found for: $($profile.email)"
