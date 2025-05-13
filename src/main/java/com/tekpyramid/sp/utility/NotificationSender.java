//package com.tekpyramid.sp.utility;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Component;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.tekpyramid.sp.entity.User;
//import com.tekpyramid.sp.responseDto.ResponseDto;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//
//@Component
//public class NotificationSender {
//	@Autowired
//	private JavaMailSender mailSender;
//	
//	@KafkaListener(topics = "user-created", groupId = "my-group")
//	public void userCreated(ResponseDto responseDto) throws MessagingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		objectMapper.registerModule(new JavaTimeModule());
//		User user = objectMapper.convertValue(responseDto.getData(),
//				User.class);
//		MimeMessage message = mailSender.createMimeMessage();
//		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//		helper.setTo(user.getEmail());
//		helper.setSubject(" Welcome to SupportDesk, " + user.getUserName() + "!");
//
//		helper.setText(String.format(
//				"""
//						    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
//						        <h2 style="color: #2C3E50;">Hi %s,</h2>
//
//						        <p style="font-size: 16px;">🎉 <strong>Welcome to SupportDesk!</strong></p>
//						        <p>We're excited to have you on board. Your account has been successfully created.</p>
//
//						        <div style="border-left: 4px solid #3498db; padding-left: 10px; margin: 10px 0;">
//						            <h3 style="color: #3498db;">📝 Your Details</h3>
//						            <p><strong>👤 Name:</strong> %s</p>
//						            <p><strong>📧 Email:</strong> %s</p>
//						            <p><strong>📞 Phone:</strong> %s</p>
//						        </div>
//
//						        <p>You can now create issues easily!</p>
//						        <p>👉 <a href="http://localhost:8080" style="color: #3498db; text-decoration: none;">please reset your password</a></p>
//
//						        <hr>
//						        <p style="font-size: 14px; color: #777;">Regards,<br><strong>SupportDesk Portal</strong></p>
//						    </div>
//						""",
//				user.getUserName(), user.getUserName(), user.getEmail(), user.getPassword()), true);
//
//		mailSender.send(message);
//	}
//
////	@KafkaListener(topics = "application-booked", groupId = "my-group")
////	public void sendNotification(ResponseDto appointment) throws MessagingException {
////		ObjectMapper objectMapper = new ObjectMapper();
////		 objectMapper.registerModule(new JavaTimeModule());
//////		AppointmentResponseDto appointmentResponseDto = objectMapper.convertValue(appointment.getData(),
//////				AppointmentResponseDto.class);
////
////		MimeMessage message = mailSender.createMimeMessage();
////		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
////
//////		helper.setTo(appointmentResponseDto.getUserEmail());
////		helper.setSubject("Appointment Confirmation - BookMyDoctor");
////
////		helper.setText(
////				String.format(
////						"""
////								    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
////								        <h2 style="color: #2C3E50;">Hi %s,</h2>
////
////								        <p style="font-size: 16px;">🎉 <strong>Your appointment has been successfully booked!</strong></p>
////
////								        <div style="border-left: 4px solid #3498db; padding-left: 10px; margin: 10px 0;">
////								            <h3 style="color: #3498db;">📅 Appointment Details</h3>
////								            <p><strong>Date & Time:</strong> %s</p>
////								        </div>
////
////								        <div style="border-left: 4px solid #27ae60; padding-left: 10px; margin: 10px 0;">
////								            <h3 style="color: #27ae60;">👨‍⚕️ Doctor Details</h3>
////								            <p><strong>Name:</strong> %s</p>
////								            <p><strong>Specialty:</strong> %s</p>
////								            <p><strong>Contact:</strong> %s</p>
////								            <p><strong>Rating:</strong> %.2f ⭐</p>
////								        </div>
////
////								        <div style="border-left: 4px solid #e67e22; padding-left: 10px; margin: 10px 0;">
////								            <h3 style="color: #e67e22;">📌 Appointment Status</h3>
////								            <p><strong>Status:</strong> %s</p>
////								        </div>
////
////								        <p>If you have any questions, feel free to contact us.</p>
////
////								        <hr>
////								        <p style="font-size: 14px; color: #777;">Regards,<br><strong>BookMyDoctor Team</strong></p>
////								    </div>
////								""",
//////						appointmentResponseDto.getUserName(), appointmentResponseDto.getAppointmentDateTime(),
//////						appointmentResponseDto.getDoctorResponseDto().getDoctorName(),
//////						appointmentResponseDto.getDoctorResponseDto().getSpeciaList(),
//////						appointmentResponseDto.getDoctorResponseDto().getDoctorNumber(),
//////						appointmentResponseDto.getDoctorResponseDto().getRating(), appointmentResponseDto.getStatus()),
////				true);
////
////		mailSender.send(message);
////	}
////
////	@KafkaListener(topics = "user-creation", groupId = "my-group")
////	public void sendUserCreationNotification(ResponseDto appointment) throws MessagingException {
////		ObjectMapper objectMapper = new ObjectMapper();
////		 objectMapper.registerModule(new JavaTimeModule());
//////		UserResponseDto user = objectMapper.convertValue(appointment.getData(), UserResponseDto.class);
////		MimeMessage message = mailSender.createMimeMessage();
////		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
////
////		helper.setTo(user.getUserEmail());
////		helper.setSubject("🎉 Welcome to BookMyDoctor, " + user.getUserName() + "!");
////
////		helper.setText(String.format(
////				"""
////						    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
////						        <h2 style="color: #2C3E50;">Hi %s,</h2>
////
////						        <p style="font-size: 16px;">🎉 <strong>Welcome to BookMyDoctor!</strong></p>
////						        <p>We're excited to have you on board. Your account has been successfully created.</p>
////
////						        <div style="border-left: 4px solid #3498db; padding-left: 10px; margin: 10px 0;">
////						            <h3 style="color: #3498db;">📝 Your Details</h3>
////						            <p><strong>👤 Name:</strong> %s</p>
////						            <p><strong>📧 Email:</strong> %s</p>
////						            <p><strong>📞 Phone:</strong> %s</p>
////						            <p><strong>🎂 Age:</strong> %d</p>
////						            <p><strong>⚧ Gender:</strong> %s</p>
////						        </div>
////
////						        <p>You can now book appointments with top doctors easily!</p>
////						        <p>👉 <a href="https://bookmydoctor.com/login" style="color: #3498db; text-decoration: none;">Login to your account</a></p>
////
////						        <hr>
////						        <p style="font-size: 14px; color: #777;">Regards,<br><strong>BookMyDoctor Team</strong></p>
////						    </div>
////						""",
////				user.getUserName(), user.getUserName(), user.getUserEmail(), user.getPhoneNumber(), user.getAge(),
////				user.getGender()), true);
////
////		mailSender.send(message);
////	}
////
////	@KafkaListener(topics = "user-updation", groupId = "my-group")
////	public void sendUserUpdateNotification(ResponseDto appointment) throws MessagingException {
////		ObjectMapper objectMapper = new ObjectMapper();
////		 objectMapper.registerModule(new JavaTimeModule());
////		UserResponseDto user = objectMapper.convertValue(appointment.getData(), UserResponseDto.class);
////
////		MimeMessage message = mailSender.createMimeMessage();
////		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
////
////		helper.setTo(user.getUserEmail());
////		helper.setSubject("🔄 Profile Update Confirmation - BookMyDoctor");
////
////		helper.setText(String.format("""
////				    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
////				        <h2 style="color: #2C3E50;">Hi %s,</h2>
////
////				        <p>✅ Your profile has been successfully updated.</p>
////
////				        <div style="border-left: 4px solid #2ECC71; padding-left: 10px; margin: 10px 0;">
////				            <h3 style="color: #2ECC71;">📌 Updated Details:</h3>
////				            <p><strong>👤 Name:</strong> %s</p>
////				            <p><strong>📧 Email:</strong> %s</p>
////				            <p><strong>📞 Phone:</strong> %s</p>
////				            <p><strong>🎂 Age:</strong> %d</p>
////				            <p><strong>⚧ Gender:</strong> %s</p>
////				        </div>
////
////				        <p>If you did not make these changes, please contact support immediately.</p>
////
////				        <hr>
////				        <p style="font-size: 14px; color: #777;">Regards,<br><strong>BookMyDoctor Team</strong></p>
////				    </div>
////				""", user.getUserName(), user.getUserName(), user.getUserEmail(), user.getPhoneNumber(), user.getAge(),
////				user.getGender()), true);
////
////		mailSender.send(message);
////	}
////
////	/**
////	 * Sends a notification when an appointment is updated.
////	 */
////	@KafkaListener(topics = "notification-updation", groupId = "my-group")
////	public void sendAppointmentUpdateNotification(ResponseDto appointment1) throws MessagingException {
////		ObjectMapper objectMapper = new ObjectMapper();
////		 objectMapper.registerModule(new JavaTimeModule());
////		AppointmentResponseDto appointment = objectMapper.convertValue(appointment1.getData(),
////				AppointmentResponseDto.class);
////
////		MimeMessage message = mailSender.createMimeMessage();
////		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
////
////		helper.setTo(appointment.getUserEmail());
////		helper.setSubject("📅 Appointment Update - BookMyDoctor");
////
////		helper.setText(String.format("""
////				    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
////				        <h2 style="color: #2C3E50;">Hi %s,</h2>
////
////				        <p>🔄 Your appointment has been updated.</p>
////
////				        <div style="border-left: 4px solid #F39C12; padding-left: 10px; margin: 10px 0;">
////				            <h3 style="color: #F39C12;">📅 New Appointment Details:</h3>
////				            <p><strong>📆 Date & Time:</strong> %s</p>
////				            <p><strong>👨‍⚕️ Doctor:</strong> %s</p>
////				            <p><strong>🔹 Specialty:</strong> %s</p>
////				            <p><strong>📞 Contact:</strong> %s</p>
////				            <p><strong>⭐ Rating:</strong> %.2f</p>
////				            <p><strong>📌 Status:</strong> %s</p>
////				        </div>
////
////				        <p>If this change was not requested by you, please contact support immediately.</p>
////
////				        <hr>
////				        <p style="font-size: 14px; color: #777;">Regards,<br><strong>BookMyDoctor Team</strong></p>
////				    </div>
////				""", appointment.getUserName(), appointment.getAppointmentDateTime(),
////				appointment.getDoctorResponseDto().getDoctorName(), appointment.getDoctorResponseDto().getSpeciaList(),
////				appointment.getDoctorResponseDto().getDoctorNumber(), appointment.getDoctorResponseDto().getRating(),
////				appointment.getStatus()), true);
////
////		mailSender.send(message);
////	}
//	
//
//
//}
