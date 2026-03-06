package com.example.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final com.example.auth.repository.UserRepository userRepository;

    public EmailService(JavaMailSender mailSender, com.example.auth.repository.UserRepository userRepository) {
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public void broadcastFireAlert() {
        userRepository.findAll().forEach(user -> {
            sendFireAlert(user.getEmail());
        });
    }

    public void sendFireAlert(String toEmail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[CRITICAL] FIRE DISPATCH: Sector 04 - Incident #2026-089");

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body { font-family: 'Inter', 'Segoe UI', Arial, sans-serif; line-height: 1.5; color: #1a202c; margin: 0; padding: 0; background-color: #f7fafc; }
                            .container { max-width: 650px; margin: 30px auto; border: none; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.15); background-color: #ffffff; }
                            .header { background-color: #2d3748; color: #ffffff; padding: 30px; text-align: left; border-bottom: 4px solid #e53e3e; }
                            .header h1 { margin: 0; font-size: 20px; letter-spacing: 1px; text-transform: uppercase; color: #feb2b2; }
                            .header .incident-id { font-size: 14px; opacity: 0.8; margin-top: 5px; font-family: monospace; }
                            .content { padding: 40px; }
                            .status-badge { display: inline-block; background-color: #fff5f5; color: #c53030; padding: 6px 12px; border-radius: 9999px; font-weight: 800; font-size: 12px; margin-bottom: 20px; border: 1px solid #feb2b2; text-transform: uppercase; }
                            .data-table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 30px; font-size: 14px; }
                            .data-table th { text-align: left; padding: 12px; background-color: #edf2f7; color: #4a5568; width: 35%; border-bottom: 1px solid #e2e8f0; }
                            .data-table td { padding: 12px; border-bottom: 1px solid #e2e8f0; font-weight: 500; }
                            .directives { background-color: #ebf8ff; border-radius: 8px; padding: 25px; margin-bottom: 30px; border-left: 5px solid #3182ce; }
                            .directives h2 { margin-top: 0; font-size: 16px; color: #2b6cb0; text-transform: uppercase; }
                            .directives ul { margin: 10px 0 0 0; padding-left: 20px; color: #2c5282; }
                            .directives li { margin-bottom: 8px; }
                            .actions { text-align: center; margin-top: 40px; }
                            .btn-primary { display: inline-block; padding: 16px 32px; background-color: #e53e3e; color: #ffffff; text-decoration: none; border-radius: 8px; font-weight: 700; font-size: 15px; box-shadow: 0 4px 6px rgba(229, 62, 62, 0.2); }
                            .btn-secondary { display: block; margin-top: 15px; color: #718096; text-decoration: underline; font-size: 13px; }
                            .footer { background-color: #2d3748; padding: 25px; text-align: center; font-size: 12px; color: #a0aec0; }
                            .footer strong { color: #cbd5e0; }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>Incident Command Dispatch</h1>
                                <div class="incident-id">System Ref: #F-2026-089-SEC04</div>
                            </div>
                            <div class="content">
                                <div class="status-badge">● TIER 1 | IMMEDIATE ACTION REQUIRED</div>

                                <p style="font-size: 15px; margin-top: 0;"><strong>Officer Notification:</strong> A wildfire/fire hazard has been confirmed within your jurisdiction via multi-sensor verification. Situational awareness is mandatory.</p>

                                <table class="data-table">
                                    <tr><th>Detection Time</th><td>2026-03-06 21:05:42 (Local)</td></tr>
                                    <tr><th>Primary Location</th><td>Sector 04-B (Northern Ridge)</td></tr>
                                    <tr><th>GPS Coordinates</th><td>23.0225° N, 72.5714° E</td></tr>
                                    <tr><th>Confidence Level</th><td>98% (Thermal + Smoke Analysis)</td></tr>
                                    <tr><th>Reporting Unit</th><td>Automated Watchtower Node 12</td></tr>
                                </table>

                                <div class="directives">
                                    <h2>Operational Directives</h2>
                                    <ul>
                                        <li><strong>MOBILIZE:</strong> Dispatch Ground Units 7 and 12 immediately.</li>
                                        <li><strong>LOGISTICS:</strong> Primary response equipment to staging area ALPHA.</li>
                                        <li><strong>COMMS:</strong> Switch tactical radio to Channel 14.</li>
                                        <li><strong>PROTECTION:</strong> Verify perimeter of Gully B civilian settlement.</li>
                                    </ul>
                                </div>

                                <div class="actions">
                                    <a href="#" class="btn-primary">Launch Live Incident Map</a>
                                    <a href="#" class="btn-secondary">View Fire Spread Models & Wind Vectors</a>
                                </div>
                            </div>
                            <div class="footer">
                                <p><strong>RESTRICTED:</strong> Official Communication for Forest Service Personnel Only.</p>
                                <p>HackMind Advanced Warning Infrastructure &copy; 2026</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """;

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send fire alert email", e);
        }
    }
}
