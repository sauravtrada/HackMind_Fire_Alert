try {
    $uniqueEmail = "fire_test_$(Get-Random)@example.com"
    $regBody = @{
        name = "Fire Tester"
        email = $uniqueEmail
        password = "password123"
    } | ConvertTo-Json

    Write-Output "Registering $uniqueEmail..."
    $regResp = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/register" -Method Post -ContentType "application/json" -Body $regBody
    Write-Output "Registration successful."

    $loginBody = @{
        email = $uniqueEmail
        password = "password123"
    } | ConvertTo-Json

    Write-Output "`nLogging in..."
    $loginResp = Invoke-RestMethod -Uri "http://localhost:8010/api/auth/login" -Method Post -ContentType "application/json" -Body $loginBody
    $token = $loginResp.token
    Write-Output "Token obtained: $token"

    $headers = @{
        Authorization = "Bearer $token"
    }

    Write-Output "`nTriggering Fire Alert for 25mce014@nirmauni.ac.in..."
    $alertResp = Invoke-RestMethod -Uri "http://localhost:8010/api/alerts/fire?email=25mce014@nirmauni.ac.in" -Method Post -Headers $headers
    Write-Output "Alert Response: $alertResp"

} catch {
    Write-Error "An error occurred: $_"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $reader.BaseStream.Position = 0
        $body = $reader.ReadToEnd()
        Write-Output "Error Body: $body"
    }
}
