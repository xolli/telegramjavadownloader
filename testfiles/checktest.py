#!/usr/bin/env python3
import sys
from telethon import TelegramClient, events
from telethon.tl import types

sessiondata = open(sys.argv[2], "r")
session = sessiondata.readline().strip()
api_id = int(sessiondata.readline())
api_hash = sessiondata.readline().strip()
sessiondata.close()

client = TelegramClient(session, api_id, api_hash, request_retries=10, flood_sleep_threshold=120)
test_mode = sys.argv[1]

@events.register(events.NewMessage)
async def handler_new_message(update):
	if (test_mode == "personalstat" and "You download" in update.message.text):
		sys.exit(0)
	elif (test_mode == "allstat" and "1483105750:" in update.message.text):
		sys.exit(0)
	elif (test_mode == "helper" and "Бот для прокачивания ссылок и торрентов через телеграм" == update.message.text):
		sys.exit(0)
	elif test_mode == "simpleuserauth" and "Authorization was successful" == update.message.text:
		sys.exit(0)
	elif test_mode == "startchat" and "Hi! This is a bot that can download files and upload them to the Telegram" == update.message.text:
		sys.exit(0)
	elif test_mode == "adminuserauth" and "Hi, Admin!" == update.message.text:
		sys.exit(0)
		


@events.register(events.MessageEdited)
async def handler_update_message(update):
	if (test_mode == "done" and update.message.text == "Done!"):
		sys.exit(0)

async def send_hello_message():
	await client.send_message(sys.argv[2], "hello!")

try:
	client.start()
#	if test_mode == "simpleuserauth":
#		client.loop.run_until_complete(send_hello_message())
	client.add_event_handler(handler_new_message)
	client.add_event_handler(handler_update_message)
	client.run_until_disconnected()
finally:
	client.disconnect()
