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

    public void broadcastFireAlert(Double latitude, Double longitude, Double percentage) {
        userRepository.findAll().forEach(user -> {
            sendFireAlert(user.getEmail(), latitude, longitude, percentage);
        });
    }

    public void sendFireAlert(String toEmail, Double latitude, Double longitude, Double percentage) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("[ALERT] RISK ASSESSMENT REQUIRED: Sector 04 - Ref #2026-089");

            String coords = (latitude != null && longitude != null)
                    ? String.format("%.4f° N, %.4f° E", latitude, longitude)
                    : "Unknown";
            String conf = (percentage != null) ? String.format("%.1f%%", percentage) : "Unknown";

            String htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <style>
                            body { font-family: 'Inter', 'Segoe UI', Arial, sans-serif; line-height: 1.5; color: #1a202c; margin: 0; padding: 0; background-color: #f7fafc; }
                            .container { max-width: 650px; margin: 30px auto; border: none; border-radius: 12px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.15); background-color: #ffffff; }
                            .header { background-color: #2d3748; color: #ffffff; padding: 30px; text-align: left; border-bottom: 4px solid #dd6b20; }
                            .header h1 { margin: 0; font-size: 20px; letter-spacing: 1px; text-transform: uppercase; color: #fbd38d; }
                            .header .incident-id { font-size: 14px; opacity: 0.8; margin-top: 5px; font-family: monospace; }
                            .content { padding: 40px; }
                            .status-badge { display: inline-block; background-color: #fffaf0; color: #c05621; padding: 6px 12px; border-radius: 9999px; font-weight: 800; font-size: 12px; margin-bottom: 20px; border: 1px solid #fbd38d; text-transform: uppercase; }
                            .data-table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 30px; font-size: 14px; }
                            .data-table th { text-align: left; padding: 12px; background-color: #edf2f7; color: #4a5568; width: 35%; border-bottom: 1px solid #e2e8f0; }
                            .data-table td { padding: 12px; border-bottom: 1px solid #e2e8f0; font-weight: 500; }
                            .directives { background-color: #ebf8ff; border-radius: 8px; padding: 25px; margin-bottom: 30px; border-left: 5px solid #3182ce; }
                            .directives h2 { margin-top: 0; font-size: 16px; color: #2b6cb0; text-transform: uppercase; }
                            .directives ul { margin: 10px 0 0 0; padding-left: 20px; color: #2c5282; }
                            .directives li { margin-bottom: 8px; }
                            .actions { text-align: center; margin-top: 40px; }
                            .btn-primary { display: inline-block; padding: 16px 32px; background-color: #dd6b20; color: #ffffff; text-decoration: none; border-radius: 8px; font-weight: 700; font-size: 15px; box-shadow: 0 4px 6px rgba(221, 107, 32, 0.2); }
                            .btn-secondary { display: block; margin-top: 15px; color: #718096; text-decoration: underline; font-size: 13px; }
                            .footer { background-color: #2d3748; padding: 25px; text-align: center; font-size: 12px; color: #a0aec0; }
                            .footer strong { color: #cbd5e0; }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <h1>Risk Assessment Directives</h1>
                                <div class="incident-id">System Ref: #R-2026-090-SEC04</div>
                            </div>
                            <div class="content">
                                <div class="status-badge">● PREVENTIVE INSPECTION REQUIRED</div>

                                <p style="font-size: 15px; margin-top: 0;"><strong>Officer Notification:</strong> A potential fire risk has been identified in your jurisdiction by the Forest Head. Immediate inspection and preventive action are required to mitigate any hazards.</p>

                                <table class="data-table">
                                    <tr><th>Primary Location</th><td>Sector 04-B (Northern Ridge)</td></tr>
                                    <tr><th>GPS Coordinates</th><td>"""
                    + coords + """
                            </td></tr>
                                    <tr><th>Risk Rating</th><td>""" + conf
                    + """
                                    </td></tr>
                                        </table>

                                        <div class="directives">
                                            <h2>Operational Directives</h2>
                                            <ul>
                                                <li><strong>INSPECT:</strong> Dispatch a team to the coordinates for immediate assessment.</li>
                                                <li><strong>ACTION:</strong> Clear dry brush and assess potential ignition sources.</li>
                                                <li><strong>REPORT:</strong> Provide status update upon completion of assessment.</li>
                                            </ul>
                                        </div>

                                        <div class="actions">
                                            <a href="#" class="btn-primary">Launch Risk Assessment Map</a>
                                            <a href="#" class="btn-secondary">View Area Topography</a>
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
