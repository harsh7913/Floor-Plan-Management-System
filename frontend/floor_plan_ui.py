import requests
from tabulate import tabulate
from colorama import Fore, Style, init

init(autoreset=True)  # Initialize colorama for automatic reset

def beautify_floor_plans(floor_plans):
    formatted_floor_plans = []

    for floor in floor_plans:
        formatted_floor_plan = [f"{Fore.CYAN}Floor {floor['floorNumber']}{Style.RESET_ALL}"]
        formatted_rooms = []
        for room in floor['rooms']:
            capacity_color = get_capacity_color(room['capacity'])
            # Include the room ID in the output
            formatted_rooms.append(f"{capacity_color}| Room ID: {room['roomId']} - {room['name']} (Capacity: {room['capacity']}){Style.RESET_ALL} ")
        formatted_floor_plan.extend(formatted_rooms)
        formatted_floor_plans.append(formatted_floor_plan)

    return tabulate(formatted_floor_plans, tablefmt='fancy_grid')

def get_capacity_color(capacity):
    if capacity > 20:
        return Fore.RED  # High capacity, use red color
    elif capacity > 10:
        return Fore.YELLOW  # Medium capacity, use yellow color
    else:
        return Fore.GREEN  # Low capacity, use green color

def colorful_input(prompt):
    return input(f"{Fore.BLUE}{Style.BRIGHT}{prompt}{Style.RESET_ALL}")

def main():
    api_url = "http://localhost:8080/api/floor-plan"
    print("Welcome to Floor Plan Management System")
    while True:
        # Get the username from the user
        username = colorful_input("Enter username: ")

        # Get the version from the user
        version = colorful_input("Enter the version: ")

        # Ask for the operation
        print("1. View Floor Plans")
        print("2. Update Floor Plan (Admin only)")
        print("3. Recommend Rooms")
        print("4. Book a Room")
        print("0. Exit")

        operation = colorful_input("Select operation (1, 2, 3, 4, or 0 to exit): ")

        if operation == '0':
            print("Exiting the program.")
            break

        if operation == '1':
            # Perform a GET request to view floor plans
            get_url = f"{api_url}?username={username}&version={version}"
            response = requests.get(get_url)
            if response.status_code == 200:
                try:
                    floor_plans = response.json()
                    print("Floor Plans:")
                    print(beautify_floor_plans(floor_plans))
                except requests.exceptions.JSONDecodeError:
                    print("The Input (Username/Version is invalid)")
            else:
                print(f"GET request failed with status code {response.status_code}")

        elif operation == '2':
            # Perform a POST request to update floor plans
            if username != 'admin':
                print("Only admin can update the floor plan")
                continue
            num_floors = int(colorful_input("Enter the number of floors: "))
            updated_floor_plans = []
            for i in range(1, num_floors + 1):
                num_rooms = int(colorful_input(f"Enter the number of rooms for Floor {i}: "))
                rooms = []
                for j in range(1, num_rooms + 1):
                    room_name = colorful_input(f"Enter the name for Room {j} on Floor {i}: ")
                    room_capacity = int(colorful_input(f"Enter the capacity for Room {j} on Floor {i}: "))
                    rooms.append({"name": room_name, "capacity": room_capacity})
                updated_floor_plans.append({"floorNumber": i, "rooms": rooms})
            payload = {"username": username, "version": version, "floorDTOs": updated_floor_plans}
            response = requests.post(api_url + '/update', json=payload)
            if response.status_code == 200:
                print("Floor plan updated successfully")
            else:
                print(f"POST request failed with status code {response.status_code}")

        elif operation == '3':
            # Recommend rooms based on participants and last booked room
            participants = int(colorful_input("Enter the number of participants: "))
            last_room_id = colorful_input("Enter last booked room ID (or press Enter to skip): ")

            params = {"participants": participants, "lastRoomId": last_room_id}
            recommend_url = f"{api_url}/recommend-rooms"
            response = requests.get(recommend_url, params=params)
            if response.status_code == 200:
                recommended_rooms = response.json()
                if recommended_rooms:
                    print("Recommended Rooms:")
                    for i, room in enumerate(recommended_rooms, 1):
                        # Display the room ID along with the name and capacity
                        print(f"{i}. Room ID: {room['roomId']} - {room['name']} (Capacity: {room['capacity']})")
                else:
                    print("No rooms available for the given criteria.")
            else:
                print(f"GET request failed with status code {response.status_code}")

        elif operation == '4':
            # Book a selected room
            room_id = colorful_input("Enter the Room ID to book: ")
            participants = int(colorful_input("Enter the number of participants: "))

            # Ensure the version is included in the booking request
            booking_payload = {"roomId": room_id, "participants": participants, "version": version}
            book_url = f"{api_url}/book-room"
            response = requests.post(book_url, json=booking_payload)
            
            # Check response status code and print the error if it failed
            if response.status_code == 200:
                print(response.text)
            else:
                print(f"POST request failed with status code {response.status_code}")
                print("Response content:", response.text)  # Log the response content for debugging

        else:
            print("Invalid operation selected")

if __name__ == "__main__":
    main()
